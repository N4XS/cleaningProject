package dataAccess;

import exceptions.connexion.SingletonConnexionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    private static Connection instance;

    private SingletonConnection() { }

    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                instance = DriverManager.getConnection("jdbc:mysql://localhost:3306/cleaningproject", "root", "dbpassword");
                System.out.println("SingletonConnection - Nouvelle connexion établie.");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Impossible d'établir une connexion : " + e.getMessage());
            }
        } else {
            System.out.println("SingletonConnection - Connexion existante réutilisée. Connexion fermée ? " + instance.isClosed());
        }
        return instance;
    }

}