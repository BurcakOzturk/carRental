package core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    //singleton design pattern
    private static Database instance = null;
    private Connection connection = null;
    private final String DB_URL = "jdbc:postgresql://localhost:5432/car_rental"; // Veritabanı URL'si
    private final String DB_USER = "postgres"; // Veritabanı kullanıcı adı
    private final String DB_PASS = "burcak"; // Veritabanı şifresi

    // Yapılandırıcı metot
    private Database () {
        try {
            // Veritabanı bağlantısını kur
            this.connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
        } catch (SQLException e) {
            // Hata durumunda ekrana hata mesajını yazdır
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static Connection getInstance() {
        try {
            if (instance == null || instance.getConnection().isClosed()) {
                instance = new Database();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return instance.getConnection();
    }
}
