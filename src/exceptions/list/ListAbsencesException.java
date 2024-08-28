package exceptions.list;

import java.sql.SQLException;

public class ListAbsencesException extends Exception {
    public ListAbsencesException(String message, SQLException e) {
        super(message);
    }
}
