package wantsome.project.db.service;

import wantsome.project.db.DbManager;
import wantsome.project.db.dto.ClientDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDao {

    public List<ClientDto> getAll() {
        String sql = "SELECT * " +
                "FROM CLIENTS; ";

        List<ClientDto> clients = new ArrayList<>();

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

    public Optional<ClientDto> getById(long id) {
        String sql = "SELECT * FROM CLIENTS WHERE ID = ?;";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClientDto client = extractClientFromResult(rs);
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading client with id:" + id + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<ClientDto> getByName(String name) {
        String sql = "SELECT * FROM CLIENTS WHERE NAME = ?;";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ClientDto client = extractClientFromResult(rs);
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading client" + name + e.getMessage());
        }
        return Optional.empty();
    }

    public void insert(ClientDto client) {

        String sql = "INSERT INTO CLIENTS " +
                "(NAME,EMAIL,ADDRESS)" +
                "VALUES (?,?,?);";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getAddress());

            ps.execute();

        } catch (SQLException e) {
            System.err.println("Error inserting client " + client.getName() + e.getMessage());
        }

    }

    public void update(ClientDto client) {
        String sql = "UPDATE CLIENTS" +
                "SET NAME = ?, " +
                "EMAIL = ?," +
                "ADDRESS = ?" +
                "WHERE ID = ?;";

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
        String sql = "DELETE FROM CLIENTS WHERE ID = ?;";

        try (Connection connection = DbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.execute();

        } catch (SQLException e) {
            System.err.println("Error while deleting client with id:  " + id + e.getMessage());
        }

    }


    private ClientDto extractClientFromResult(ResultSet rs) throws SQLException {

        long id = rs.getLong("ID");
        String name = rs.getString("NAME");
        String email = rs.getString("EMAIL");
        String address = rs.getString("ADDRESS");

        return new ClientDto(id, name, email, address);
    }
}
