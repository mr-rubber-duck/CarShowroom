package org.example.carshowroom.DAO;

import org.example.carshowroom.Entity.Client;
import org.example.carshowroom.dbConnection.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
  dbConnection dbConn= new dbConnection();

    public void addClient(Client client) {
            String query="INSERT INTO Client (name,email,phone,address,city,state,bankAccountNumber,bankName,zip,country) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = dbConn.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1,client.getName());
            stmt.setString(2,client.getEmail());
            stmt.setString(3,client.getPhone());
            stmt.setString(4,client.getAddress());
            stmt.setString(5,client.getCity());
            stmt.setString(6,client.getState());
            stmt.setString(7,client.getBankAccountNumber());
            stmt.setString(8,client.getBankName());
            stmt.setString(9,client.getZip());
            stmt.setString(10,client.getCountry());

            stmt.executeUpdate();
            System.out.println("Client added successfully");
        }
        catch(SQLException e) {
            System.err.println("Error adding client:"+e.getMessage());
        }

    }
    public void getClientById(int id) {
        String query="SELECT * FROM Client WHERE id = ?";

        try(Connection conn = dbConn.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setEmail(rs.getString("email"));
                client.setPhone(rs.getString("phone"));
                client.setAddress(rs.getString("address"));
                client.setCity(rs.getString("city"));
                client.setState(rs.getString("state"));
                client.setBankAccountNumber(rs.getString("bankAccountNumber"));
                client.setBankName(rs.getString("bankName"));
                client.setZip(rs.getString("zip"));
                client.setCountry(rs.getString("country"));
                System.out.println(client);
            }
        }catch(SQLException e){
            System.err.println("Sorry we didn't find this clien:"+e.getMessage());
        }


    }
    public void updateClient(Client client) {
        String query="UPDATE Client SET name = ?, email = ?, phone = ?, address = ?, city = ?, state = ?, bankAccountNumber = ?, bankName = ?, zip = ?, country = ? WHERE id = ?";
        try(Connection conn = dbConn.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhone());
            stmt.setString(4, client.getAddress());
            stmt.setString(5, client.getCity());
            stmt.setString(6, client.getState());
            stmt.setString(7, client.getBankAccountNumber());
            stmt.setString(8, client.getBankName());
            stmt.setString(9, client.getZip());
            stmt.setString(10, client.getCountry());
            stmt.setInt(11, client.getId());
            stmt.executeUpdate();
            System.out.println("Client updated successfully");
        }catch(SQLException e){
            System.err.println("Error updating client:"+e.getMessage());
        }
    }
    public void deleteClient(int id) {
        String query="DELETE FROM Client WHERE id = ?";
        try(Connection conn = dbConn.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Client deleted successfully");
        }catch(SQLException e){
            System.err.println("Error deleting client:"+e.getMessage());
        }
    }
     public List<Client> getAllClients() {
        String query="SELECT * FROM Client";
        List<Client> clients = new ArrayList<>();
        try(Connection conn = dbConn.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setEmail(rs.getString("email"));
                client.setPhone(rs.getString("phone"));
                client.setAddress(rs.getString("address"));
                client.setCity(rs.getString("city"));
                client.setState(rs.getString("state"));
                client.setBankAccountNumber(rs.getString("bankAccountNumber"));
                client.setBankName(rs.getString("bankName"));
                client.setZip(rs.getString("zip"));
                client.setCountry(rs.getString("country"));
                clients.add(client);
            }
        }catch(SQLException e){
            System.err.println("Error getting clients:"+e.getMessage());
        }
        return clients;
    }
    public Client getClientByName(String name){
        String query="select * from Client where name=? ";
        try(Connection conn = dbConn.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setEmail(rs.getString("email"));
                client.setPhone(rs.getString("phone"));
                client.setAddress(rs.getString("address"));
                client.setCity(rs.getString("city"));
                client.setState(rs.getString("state"));
                client.setBankAccountNumber(rs.getString("bankAccountNumber"));
                client.setBankName(rs.getString("bankName"));
                client.setZip(rs.getString("zip"));
                client.setCountry(rs.getString("country"));
                return client;
            }
        }catch(SQLException e){
            System.err.println("Error getting client by name:"+e.getMessage());
        }
        return null;
    }
    public Client getClientByEmail(String email){
        String query="select * from Client where email=? ";
        try(Connection conn = dbConn.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setEmail(rs.getString("email"));
                client.setPhone(rs.getString("phone"));
                client.setAddress(rs.getString("address"));
                client.setCity(rs.getString("city"));
                client.setState(rs.getString("state"));
                client.setBankAccountNumber(rs.getString("bankAccountNumber"));
                client.setBankName(rs.getString("bankName"));
                client.setZip(rs.getString("zip"));
                client.setCountry(rs.getString("country"));
                return client;
            }
        }catch(SQLException e){
            System.err.println("Error getting client by email:"+e.getMessage());
        }
        return null;
    }

}
