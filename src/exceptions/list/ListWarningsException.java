package exceptions.list;

import java.sql.SQLException;

public class ListWarningsException extends Exception {
    public ListWarningsException(String message, SQLException e) {
        super(message);
    }
}
