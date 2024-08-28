package exceptions.list;

import java.sql.SQLException;

public class ListClientsException extends Exception {
    public ListClientsException(String message, SQLException e) {
        super(message);
    }
}
