package exceptions.list;

import java.sql.SQLException;

public class ListStaffMembersException extends Exception {
    public ListStaffMembersException(String message, SQLException e) {
        super(message);
    }
}
