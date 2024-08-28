package exceptions.list;

import java.sql.SQLException;

public class ListProductsException extends Exception {
    public ListProductsException(String message, SQLException e) {
        super(message);
    }
}
