package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.PaymentMethod;
import wantsome.project.db.dto.ReservationDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationDao {

    public List<ReservationDto> getAll() {
        List<ReservationDto> reservations = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM RESERVATIONS;";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(extractReservationsFromResult(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading all reservations: " + e.getMessage());
        }

        return reservations;
    }

    public List<ReservationDto> getAllOrderedByStartDate() {
        List<ReservationDto> reservations = new ArrayList<>();

        String sql = "SELECT * " +
                "FROM RESERVATIONS " +
                "ORDER BY START_DATE; ";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(extractReservationsFromResult(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading all reservations: " + e.getMessage());
        }

        return reservations;
    }

    public String getClientsName(ReservationDto reservation) {
        String name = "";
        long id = reservation.getId();
        String sql = "SELECT NAME " +
                "FROM RESERVATIONS " +
                "LEFT JOIN CLIENTS ON CLIENT_ID = CLIENTS.ID " +
                "WHERE RESERVATIONS.ID= ? ;";

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    name = rs.getString("NAME");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading all names: " + e.getMessage());
        }

        return name;
    }


    public Optional<ReservationDto> getById(long id) {

        String sql = "SELECT * FROM RESERVATIONS WHERE ID = ?;";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservationDto reservation = extractReservationsFromResult(rs);
                    return Optional.of(reservation);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading reservation " + id + ": " + e.getMessage());
        }

    }

    public List<ReservationDto> getActiveReservationsOrderedByDate() {

        List<ReservationDto> reservations = new ArrayList<>();
        Date currentDate = Date.valueOf(LocalDate.now());

        String sql = "SELECT * FROM RESERVATIONS " +
                "WHERE END_DATE >= ? " +
                "ORDER BY START_DATE;";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, currentDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservations.add(extractReservationsFromResult(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading reservations from today and further." + e.getMessage());
        }
        return reservations;
    }

    public List<ReservationDto> getInactiveReservationsOrderedByDate() {

        Date currentDate = Date.valueOf(LocalDate.now());
        List<ReservationDto> reservations = new ArrayList<>();

        String sql = "SELECT * FROM RESERVATIONS " +
                "WHERE END_DATE < ? " +
                "ORDER BY START_DATE;";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, currentDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservations.add(extractReservationsFromResult(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading inactive reservations." + e.getMessage());
        }
        return reservations;
    }

    public List<ReservationDto> getReservationsFromSpecificDate(Date date) {

        List<ReservationDto> reservations = new ArrayList<>();

        String sql = "SELECT * FROM RESERVATIONS " +
                "WHERE START_DATE <= ? " +
                "AND END_DATE >= ? ;";


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
            throw new RuntimeException("Error loading reservations from date: " + e.getMessage());
        }
        return reservations;
    }

    public List<ReservationDto> reservationsOfClientFromDate(Date date, String name) {

        List<ReservationDto> reservationsOfClient = new ArrayList<>();

        String sql = "SELECT * FROM RESERVATIONS " +
                "WHERE START_DATE >= ? " +
                "AND END_DATE <= ? " +
                "AND NAME = ?;";

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
        } catch (SQLException e) {
            throw new RuntimeException("Error loading reservations of client " + name + ", from date " + date);
        }
        return reservationsOfClient;
    }

    public void insert(ReservationDto reservation) {

        String sql = "INSERT INTO RESERVATIONS " +
                "(CLIENT_NAME, START_DATE, END_DATE, ROOM_NUMBER, " +
                "EXTRA_INFO, PAYMENT_METHOD, CREATED_AT) " +
                "VALUES (?,?,?,?,?,?,?)";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, reservation.getClientName());
            ps.setDate(2, reservation.getStartDate());
            ps.setDate(3, reservation.getEndDate());
            ps.setLong(4, reservation.getRoomNumber());
            ps.setString(5, reservation.getExtraInfo());
            ps.setString(6, reservation.getPayment().name());
            ps.setDate(7, reservation.getCreatedAt());

            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting reservation " + reservation + e.getMessage());
        }

    }

    public void update(ReservationDto reservation) {
        String sql = "UPDATE RESERVATIONS   " +
                "SET CLIENT_NAME = ?, " +
                "START_DATE = ?, " +
                "END_DATE = ?, " +
                "ROOM_NUMBER = ?, " +
                "EXTRA_INFO = ?, " +
                "PAYMENT_METHOD = ? " +
                "WHERE ID = ?;";


        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {


            //data la care a fost creata rezervarea nu se poate modifica
            ps.setString(1, reservation.getClientName());
            ps.setDate(2, reservation.getStartDate());
            ps.setDate(3, reservation.getEndDate());
            ps.setLong(4, reservation.getRoomNumber());
            ps.setString(5, reservation.getExtraInfo());
            ps.setString(6, reservation.getPayment().name());
            ps.setLong(7, reservation.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error while updating reservation " + reservation + e.getMessage());
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM RESERVATIONS WHERE ID = ?;";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting reservation with id:  " + id + e.getMessage());
        }

    }

    private ReservationDto extractReservationsFromResult(ResultSet rs) throws SQLException {

        long id = rs.getLong("ID");
        String clientName = rs.getString("CLIENT_NAME");
        Date startDate = rs.getDate("START_DATE");
        Date endDate = rs.getDate("END_DATE");
        long roomId = rs.getLong("ROOM_NUMBER");
        String extraInfo = rs.getString("EXTRA_INFO");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("PAYMENT_METHOD"));
        Date createdAt = rs.getDate("CREATED_AT");

        return new ReservationDto(id, clientName, startDate, endDate, roomId, extraInfo, paymentMethod, createdAt);
    }
}
