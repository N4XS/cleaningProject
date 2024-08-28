package exceptions.connexion;

import java.sql.SQLException;

public class SingletonConnexionException extends Exception {
    public SingletonConnexionException(String message, SQLException e) {
        super(message);
    }

    public SingletonConnexionException(String message) {
        super(message);
    }
}
