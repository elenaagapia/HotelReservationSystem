package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.RoomDto;
import wantsome.project.db.dto.RoomTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDao {
    public List<RoomDto> getAll() {
        String sql = "SELECT * " +
                "FROM ROOMS " +
                "ORDER BY NUMBER";

        List<RoomDto> rooms = new ArrayList<>();

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rooms.add(extractRoomFromResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error loading all rooms: " + e.getMessage());
        }

        return rooms;
    }


    public List<RoomDto> getAllAvailable(Date startDate, Date endDate) { // sau mai bine o perioada?

        String sql = "SELECT R.NUMBER FROM ROOMS R" +
                "LEFT OUTER JOIN RESERVATIONS RES" +
                "ON R.NUMBER = RES.ROOM_NUMBER" +
                "WHERE RES.START_DATE > ? " + //endDate + //ca sa ma asigur ca rezervarea noua e inaintea celei deja existente
                "OR RES.END_DATE < ?";// + startDate; // ca sa ma asigur ca rezervarea noua se face dupa ce se termina cea veche

        List<RoomDto> availableRooms = new ArrayList<>();

        try (Connection con = DbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, endDate);
            ps.setDate(2, startDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    availableRooms.add(extractRoomFromResult(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading available rooms: " + e.getMessage());
        }


        return availableRooms;
    }

    public Optional<RoomDto> get(long number) {
        String sql = "SELECT * FROM ROOMS WHERE NUMBER = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, number);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RoomDto room = extractRoomFromResult(rs);
                    return Optional.of(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void insert(RoomDto room) {

        String sql = "INSERT INTO ROOMS " +
                "(ROOM_TYPE_DESCRIPTION, EXTRA_INFO)" +
                "VALUES (?,?)";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, room.getRoomType().name());
            ps.setString(2, room.getExtraInfo());

            ps.execute();

        } catch (SQLException e) {
            System.err.println("Error inserting room nr:" + room.getNumber() + e.getMessage());
        }

    }

    public void update(RoomDto room) {
        String sql = "UPDATE ROOMS" +
                "SET ROOM_TYPE_DESCRIPTION = ?," +
                "EXTRA_INFO = ?" +
                "WHERE NUMBER = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, room.getRoomType().name());
            ps.setString(2, room.getExtraInfo());
            ps.setLong(3, room.getNumber());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating room nr: " + room.getNumber() + e.getMessage());
        }
    }

    public void delete(long number) {
        String sql = "DELETE FROM ROOMS WHERE NUMBER = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, number);
        } catch (SQLException e) {
            System.err.println("Error while deleting room with number:  " + number + e.getMessage());
        }

    }


    private RoomDto extractRoomFromResult(ResultSet rs) throws SQLException {

        long number = rs.getLong("NUMBER");
        RoomTypes roomTypeDesc = RoomTypes.valueOf(rs.getString("ROOM_TYPE_DESCRIPTION"));
        String extraInfo = rs.getString("EXTRA_INFO");

        return new RoomDto(number, roomTypeDesc, extraInfo);
    }
}
