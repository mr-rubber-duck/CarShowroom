package org.example.carshowroom.dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {
    private String url="jdbc:postgresql://localhost:5432/carShowroom";
    private String user="postgres";
    private String password="Pass123";

    public Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}