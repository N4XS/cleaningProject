package exceptions.list;

import java.sql.SQLException;

public class ListCitiesException extends Exception {
    public ListCitiesException(String message, SQLException e) {
        super(message);
    }
}
