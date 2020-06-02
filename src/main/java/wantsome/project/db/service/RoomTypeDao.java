package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.RoomTypeDto;
import wantsome.project.db.dto.RoomTypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomTypeDao {
    public List<RoomTypeDto> getAll() {
        String sql = "SELECT * " +
                "FROM ROOM_TYPES;";

        List<RoomTypeDto> roomType = new ArrayList<>();

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

    public Optional<RoomTypeDto> get(RoomTypes type) {
        String sql = "SELECT * FROM ROOM_TYPES WHERE DESCRIPTION = ?;";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type.name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RoomTypeDto roomType = extractRoomTypeFromResult(rs);
                    return Optional.of(roomType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void insert(RoomTypeDto roomType) {

        String sql = "INSERT INTO ROOM_TYPES " +
                "(DESCRIPTION,PRICE,CAPACITY)" +
                "VALUES (?,?,?);";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, roomType.getType().name());
            ps.setDouble(2, roomType.getPrice());
            ps.setInt(3, roomType.getCapacity());

            ps.execute();

        } catch (SQLException e) {
            System.err.println("Error inserting room type " + roomType.getType() + e.getMessage());
        }

    }

    public void update(RoomTypeDto roomType) {
        String sql = "UPDATE ROOM_TYPES" +
                "SET PRICE = ?," +
                "CAPACITY = ?" +
                "WHERE DESCRIPTION = ?;";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, roomType.getPrice());
            ps.setInt(2, roomType.getCapacity());
            ps.setString(3, roomType.getType().name());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating room type:  " + roomType.getType() + e.getMessage());
        }
    }

    public void delete(RoomTypes type) {
        String sql = "DELETE FROM ROOM_TYPES WHERE DESCRIPTION = ?;";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, type.name());
            ps.execute();
        } catch (SQLException e) {
            System.err.println("Error while deleting room type:  " + type + e.getMessage());
        }

    }


    private RoomTypeDto extractRoomTypeFromResult(ResultSet rs) throws SQLException {

        RoomTypes type = RoomTypes.valueOf(rs.getString("DESCRIPTION"));
        double price = rs.getDouble("PRICE");
        int capacity = rs.getInt("CAPACITY");

        return new RoomTypeDto(type, price, capacity);
    }
}
