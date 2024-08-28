package exceptions.list;

import java.sql.SQLException;

public class ListMaterialsOrdersException extends Exception {
    public ListMaterialsOrdersException(String message, SQLException e) {
        super(message);
    }
}
