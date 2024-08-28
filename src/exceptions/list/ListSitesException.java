package exceptions.list;

import java.sql.SQLException;

public class ListSitesException extends Exception {
    public ListSitesException(String message, SQLException e) {
        super(message);
    }
}
