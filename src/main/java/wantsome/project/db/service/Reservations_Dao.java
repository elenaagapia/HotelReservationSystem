package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.ReservationsDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Reservations_Dao {
    public List<ReservationsDto> getAll() {
        String sql = "SELECT * " +
                "FROM RESERVATIONS " +
                "ORDER BY START_DATE";

        List<ReservationsDto> reservations = new ArrayList<>();

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

    public Optional<ReservationsDto> get(long id) {
        String sql = "SELECT * FROM RESERVATIONS WHERE ID = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservationsDto reservation = extractReservationsFromResult(rs);
                    return Optional.of(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void insert(ReservationsDto reservation) {

        String sql = "INSERT INTO RESERVATIONS " +
                "(CLIENT_ID,START_DATE,END_DATE,ROOM_ID," +
                "EXTRA_INFO,EXTRA_FACILITY, PAYMENT_METHOD, CREATED_AT)" +
                "VALUES (?,?,?,?,?,?,?,?)";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, reservation.getClientId());
            ps.setDate(2, reservation.getStartDate());
            ps.setDate(3, reservation.getEndDate());
            ps.setLong(4, reservation.getRoomId());
            ps.setString(5, reservation.getExtraInfo());
            ps.setString(6, reservation.getExtraFacilities());
            ps.setString(7, reservation.getPayment());
            ps.setDate(8, reservation.getCreatedAt());

            ps.execute();

        } catch (SQLException e) {
            System.err.println("Error inserting reservation " + reservation + e.getMessage());
        }

    }

    public void update(ReservationsDto reservation) {
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

            //data la crea a fost creata rezervarea nu se poate modifica
            ps.setLong(1, reservation.getClientId());
            ps.setDate(2, reservation.getStartDate());
            ps.setDate(3, reservation.getEndDate());
            ps.setLong(4, reservation.getRoomId());
            ps.setString(5, reservation.getExtraInfo());
            ps.setString(6, reservation.getExtraFacilities());
            ps.setString(7, reservation.getPayment());
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


    private ReservationsDto extractReservationsFromResult(ResultSet rs) throws SQLException {

        long id = rs.getLong("ID");
        long clientId = rs.getLong("CLIENT_ID");
        Date startDate = rs.getDate("START_DATE");
        Date endDate = rs.getDate("END_DATE");
        long roomId = rs.getLong("ROOM_ID");
        String extraInfo = rs.getString("EXTRA_INFO");
        String extraFacilities = rs.getString("EXTRA_FACILITY");
        String paymentMethod = rs.getString("PAYMENT_METHOD");
        Date createdAt = rs.getDate("CREATED_AT");

        return new ReservationsDto(id, clientId, startDate, endDate, roomId, extraInfo, extraFacilities, paymentMethod, createdAt);
    }
}
