package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.Room_typesDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Room_Types_Dao {
    public List<Room_typesDto> getAll() {
        String sql = "SELECT * " +
                "FROM ROOM_TYPES " +
                "ORDER BY PRICE";

        List<Room_typesDto> roomType = new ArrayList<>();

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roomType.add(extractRoomTypeFromResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error loading all room types: " + e.getMessage());
        }

        return roomType;
    }

    public Optional<Room_typesDto> get(String description) {
        String sql = "SELECT * FROM ROOM_TYPES WHERE DESCRIPTION = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, description);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Room_typesDto roomType = extractRoomTypeFromResult(rs);
                    return Optional.of(roomType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void insert(Room_typesDto roomType) {

        String sql = "INSERT INTO ROOM_TYPES " +
                "(DESCRIPTION,PRICE,CAPACITY)" +
                "VALUES (?,?,?)";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement prepstmnt = connection.prepareStatement(sql)) {

            prepstmnt.setString(1, roomType.getDescription());
            prepstmnt.setDouble(2, roomType.getPrice());
            prepstmnt.setInt(3, roomType.getCapacity());

            prepstmnt.execute();

        } catch (SQLException e) {
            System.err.println("Error inserting room type " + roomType.getDescription() + e.getMessage());
        }

    }

    public void update(Room_typesDto roomType) {
        String sql = "UPDATE ROOM_TYPES" +
                "SET PRICE = ?," +
                "CAPACITY = ?" +
                "WHERE DESCRIPTION = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement prepstmnt = connection.prepareStatement(sql)) {

            prepstmnt.setDouble(1, roomType.getPrice());
            prepstmnt.setInt(2, roomType.getCapacity());
            prepstmnt.setString(3, roomType.getDescription());

            prepstmnt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating room type:  " + roomType.getDescription() + e.getMessage());
        }
    }

    public void delete(String description) {
        String sql = "DELETE FROM ROOM_TYPES WHERE DESCRIPTION = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, description);
        } catch (SQLException e) {
            System.err.println("Error while deleting room type:  " + description + e.getMessage());
        }

    }


    private Room_typesDto extractRoomTypeFromResult(ResultSet rs) throws SQLException {

        String description = rs.getString("DESCRIPTION");
        double price = rs.getDouble("PRICE");
        int capacity = rs.getInt("CAPACITY");

        return new Room_typesDto(description, price, capacity);
    }
}
