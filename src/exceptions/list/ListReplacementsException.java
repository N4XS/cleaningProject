package exceptions.list;

import java.sql.SQLException;

public class ListReplacementsException extends Exception {
    public ListReplacementsException(String message, SQLException e) {
        super(message);
    }
}
