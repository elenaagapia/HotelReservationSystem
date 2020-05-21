package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.ExtraServices;
import wantsome.project.db.dto.Extra_facilitiesDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Extra_facilities_Dao {
    public List<Extra_facilitiesDto> getAll() {
        String sql = "SELECT * " +
                "FROM EXTRA_FACILITIES ";

        List<Extra_facilitiesDto> facilities = new ArrayList<>();

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                facilities.add(extractFacilityFromResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error loading all facilities: " + e.getMessage());
        }

        return facilities;
    }

    public Optional<Extra_facilitiesDto> get(long id) {
        String sql = "SELECT * FROM EXTRA_FACILITIES WHERE FACILITY = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Extra_facilitiesDto facility = extractFacilityFromResult(rs);
                    return Optional.of(facility);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void insert(Extra_facilitiesDto facility) {

        String sql = "INSERT INTO EXTRA_FACILITIES " +
                "(FACILITY, PRICE)" +
                "VALUES (?,?)";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement prepstmnt = connection.prepareStatement(sql)) {

            prepstmnt.setString(1, facility.getFacility().name());
            prepstmnt.setDouble(2, facility.getPrice());

            prepstmnt.execute();

        } catch (SQLException e) {
            System.err.println("Error inserting facility:" + facility.getFacility() + e.getMessage());
        }

    }

    public void update(Extra_facilitiesDto facility) {
        String sql = "UPDATE EXTRA_FACILITIES" +
                "SET PRICE = ?" +
                "WHERE FACILITY = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement prepstmnt = connection.prepareStatement(sql)) {

            prepstmnt.setDouble(1, facility.getPrice());
            prepstmnt.setString(2, facility.getFacility().name());

            prepstmnt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating facility: " + facility.getFacility() + e.getMessage());
        }
    }

    public void delete(ExtraServices facility) {
        String sql = "DELETE FROM EXTRA_FACILITIES WHERE FACILITY = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, facility.name());
        } catch (SQLException e) {
            System.err.println("Error while deleting facility:  " + facility + e.getMessage());
        }

    }


    private Extra_facilitiesDto extractFacilityFromResult(ResultSet rs) throws SQLException {

        ExtraServices facility = ExtraServices.valueOf(rs.getString("FACILITY"));
        double price = rs.getDouble("PRICE");

        return new Extra_facilitiesDto(facility, price);
    }
}
