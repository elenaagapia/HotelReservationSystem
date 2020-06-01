package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.ExtraServices;
import wantsome.project.db.dto.PaymentMethod;
import wantsome.project.db.dto.ReservationDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReservationDao {

    public List<ReservationDto> getAll() {
        List<ReservationDto> reservations = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM RESERVATIONS " +
                "ORDER BY START_DATE";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(extractReservationsFromResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error loading all reservations: " + e.getMessage());
        }

        return reservations;
    }

    public Optional<ReservationDto> get(long id) {

        String sql = "SELECT * FROM RESERVATIONS WHERE ID = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservationDto reservation = extractReservationsFromResult(rs);
                    return Optional.of(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<ReservationDto> getActiveReservations() {

        List<ReservationDto> reservations = new ArrayList<>();
        Date currentDate = Date.valueOf(LocalDate.now());

        String sql = "SELECT * FROM RESERVATIONS " +
                "WHERE START_DATE >= ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, currentDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservations.add(extractReservationsFromResult(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading reservations from today and further." + e.getMessage());
        }
        return reservations;
    }

    public List<ReservationDto> getInactiveReservations() {

        Date currentDate = Date.valueOf(LocalDate.now());
        List<ReservationDto> reservations = new ArrayList<>();

        String sql = "SELECT * FROM RESERVATIONS " +
                "WHERE END_DATE < ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, currentDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservations.add(extractReservationsFromResult(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading inactive reservations." + e.getMessage());
        }
        return reservations;
    }

    public List<ReservationDto> getReservationsFromSpecificDate(Date date) {

        List<ReservationDto> reservations = new ArrayList<>();

        String sql = "SELECT * FROM RESERVATIONS " +
                "WHERE START_DATE <= ?" +
                "AND END_DATE >= ? ";


        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, date);
            ps.setDate(2, date);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservations.add(extractReservationsFromResult(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading reservations from date: " + e.getMessage());
        }
        return reservations;
    }

    //TODO: SA O TRATEZ MAI INCOLO...
    public List<ReservationDto> reservationsOfClientFromDate(Date date, String name) throws SQLException {

        List<ReservationDto> reservationsOfClient = new ArrayList<>();

        String sql = "SELECT * FROM RESERVATIONS" +
                "WHERE START_DATE >= ? " +
                "AND END_DATE <= ? " +
                "AND NAME = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, date);
            ps.setDate(2, date);
            ps.setString(3, name);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservationsOfClient.add(extractReservationsFromResult(rs));
                }
            }
        }
        return reservationsOfClient;
    }

    public void insert(ReservationDto reservation) {

        String sql = "INSERT INTO RESERVATIONS " +
                "(CLIENT_ID,START_DATE,END_DATE,ROOM_ID," +
                "EXTRA_INFO,EXTRA_FACILITY, PAYMENT_METHOD, CREATED_AT)" +
                "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            //stream joining for EXTRA_FACILITY field
            String joinedEnumValues = Stream.of(reservation.getExtraFacilities())
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            ps.setLong(1, reservation.getClientId());
            ps.setDate(2, reservation.getStartDate());
            ps.setDate(3, reservation.getEndDate());
            ps.setLong(4, reservation.getRoomNumber());
            ps.setString(5, reservation.getExtraInfo());
            ps.setString(6, joinedEnumValues);
            ps.setString(7, reservation.getPayment().name());
            ps.setDate(8, reservation.getCreatedAt());

            ps.execute();

        } catch (SQLException e) {
            System.err.println("Error inserting reservation " + reservation + e.getMessage());
        }

    }

    public void update(ReservationDto reservation) {
        String sql = "UPDATE RESERVATIONS   " +
                "SET CLIENT_ID = ?, " +
                "START_DATE = ?," +
                "END_DATE = ?," +
                "ROOM_ID = ?," +
                "EXTRA_INFO = ?," +
                "EXTRA_FACILITY = ?," +
                "PAYMENT_METHOD = ?" +
                "WHERE ID = ?";


        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            //stream joining for EXTRA_FACILITY field
            String joinedEnumValues = Stream.of(reservation.getExtraFacilities())
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            //data la crea a fost creata rezervarea nu se poate modifica
            ps.setLong(1, reservation.getClientId());
            ps.setDate(2, reservation.getStartDate());
            ps.setDate(3, reservation.getEndDate());
            ps.setLong(4, reservation.getRoomNumber());
            ps.setString(5, reservation.getExtraInfo());
            ps.setString(6, joinedEnumValues);
            ps.setString(7, reservation.getPayment().name());
            ps.setLong(8, reservation.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating reservation " + reservation + e.getMessage());
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM RESERVATIONS WHERE ID = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
        } catch (SQLException e) {
            System.err.println("Error while deleting reservation with id:  " + id + e.getMessage());
        }

    }


    private ReservationDto extractReservationsFromResult(ResultSet rs) throws SQLException {

        List<ExtraServices> facilities = new ArrayList<>();

        long id = rs.getLong("ID");
        long clientId = rs.getLong("CLIENT_ID");
        Date startDate = rs.getDate("START_DATE");
        Date endDate = rs.getDate("END_DATE");
        long roomId = rs.getLong("ROOM_ID");
        String extraInfo = rs.getString("EXTRA_INFO");
        String[] services = rs.getString("EXTRA_FACILITY").split(",");

        //transforming from String to ExtraServices
        for (String facility : services) {
            facilities.add(ExtraServices.valueOf(facility));
        }

        PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("PAYMENT_METHOD"));
        Date createdAt = rs.getDate("CREATED_AT");

        return new ReservationDto(id, clientId, startDate, endDate, roomId, extraInfo, facilities, paymentMethod, createdAt);
    }
}
