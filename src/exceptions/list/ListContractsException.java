package exceptions.list;

import java.sql.SQLException;

public class ListContractsException extends Exception {
    public ListContractsException(String message, SQLException e) {
        super(message);
    }
}
