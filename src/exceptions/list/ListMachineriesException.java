package exceptions.list;

import java.sql.SQLException;

public class ListMachineriesException extends Exception {
    public ListMachineriesException(String message, SQLException e) {
        super(message);
    }
}
