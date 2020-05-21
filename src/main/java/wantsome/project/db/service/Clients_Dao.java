package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.ClientsDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Clients_Dao {

    public List<ClientsDto> getAll() {
        String sql = "SELECT * " +
                "FROM CLIENTS " +
                "ORDER BY NAME";

        List<ClientsDto> clients = new ArrayList<>();

        try (Connection conn = DbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clients.add(extractClientFromResult(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error loading all clients: " + e.getMessage());
        }

        return clients;
    }

    public Optional<ClientsDto> get(long id) {
        String sql = "SELECT * FROM CLIENTS WHERE ID = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClientsDto client = extractClientFromResult(rs);
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void insert(ClientsDto client) {

        String sql = "INSERT INTO CLIENTS " +
                "(NAME,EMAIL,ADDRESS)" +
                "VALUES (?,?,?)";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement prepstmnt = connection.prepareStatement(sql)) {

            prepstmnt.setString(1, client.getName());
            prepstmnt.setString(2, client.getEmail());
            prepstmnt.setString(3, client.getAddress());

            prepstmnt.execute();

        } catch (SQLException e) {
            System.err.println("Error inserting client " + client + e.getMessage());
        }

    }

    public void update(ClientsDto client) {
        String sql = "UPDATE CLIENTS" +
                "SET NAME = ?, " +
                "EMAIL = ?," +
                "ADDRESS = ?" +
                "WHERE ID = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getAddress());
            ps.setLong(4, client.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error while updating client " + client.getName() + e.getMessage());
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM CLIENTS WHERE ID = ?";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
        } catch (SQLException e) {
            System.err.println("Error while deleting client with id:  " + id + e.getMessage());
        }

    }


    private ClientsDto extractClientFromResult(ResultSet rs) throws SQLException {

        long id = rs.getLong("ID");
        String name = rs.getString("NAME");
        String email = rs.getString("EMAIL");
        String address = rs.getString("ADDRESS");

        return new ClientsDto(id, name, email, address);
    }
}
