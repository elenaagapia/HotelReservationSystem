package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.RoomsDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Rooms_Dao {
    public List<RoomsDto> getAll() {
        String sql = "SELECT * " +
                "FROM ROOMS " +
                "ORDER BY ID";

        List<RoomsDto> rooms = new ArrayList<>();

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rooms.add(extractRoomFromResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error loading all clients: " + e.getMessage());
        }

        return rooms;
    }

    public Optional<RoomsDto> get(long id) {
        String sql = "SELECT * FROM ROOMS WHERE ID = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RoomsDto room = extractRoomFromResult(rs);
                    return Optional.of(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void insert(RoomsDto room) {

        String sql = "INSERT INTO ROOMS " +
                "(ID, ROOM_TYPE_DESCRIPTION, EXTRA_INFO)" +
                "VALUES (?,?,?)";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement prepstmnt = connection.prepareStatement(sql)) {

            prepstmnt.setLong(1, room.getId());
            prepstmnt.setString(2, room.getRoomType());
            prepstmnt.setString(3, room.getExtraInfo());

            prepstmnt.execute();

        } catch (SQLException e) {
            System.err.println("Error inserting room nr:" + room.getId() + e.getMessage());
        }

    }

    public void update(RoomsDto room) {
        String sql = "UPDATE ROOMS" +
                "SET ROOM_TYPE_DESCRIPTION = ?," +
                "EXTRA_INFO = ?" +
                "WHERE ID = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement prepstmnt = connection.prepareStatement(sql)) {

            prepstmnt.setString(1, room.getRoomType());
            prepstmnt.setString(2, room.getExtraInfo());
            prepstmnt.setLong(3, room.getId());

            prepstmnt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating room nr: " + room.getId() + e.getMessage());
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM ROOMS WHERE ID = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
        } catch (SQLException e) {
            System.err.println("Error while deleting room with id:  " + id + e.getMessage());
        }

    }


    private RoomsDto extractRoomFromResult(ResultSet rs) throws SQLException {

        long id = rs.getLong("ID");
        String roomTypeDesc = rs.getString("ROOM_TYPE_DESCRIPTION");
        String extraInfo = rs.getString("EXTRA_INFO");

        return new RoomsDto(id, roomTypeDesc, extraInfo);
    }
}
