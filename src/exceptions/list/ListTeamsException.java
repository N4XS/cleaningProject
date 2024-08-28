package exceptions.list;

import java.sql.SQLException;

public class ListTeamsException extends Exception {
    public ListTeamsException(String message, SQLException e) {
        super(message);
    }
}
