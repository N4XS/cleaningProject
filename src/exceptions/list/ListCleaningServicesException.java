package exceptions.list;

import java.sql.SQLException;

public class ListCleaningServicesException extends Exception {
    public ListCleaningServicesException(String message, SQLException e) {
        super(message);
    }
}
