package exceptions.insert;

import java.sql.SQLException;

public class InsertStaffMemberException extends Exception{
    public InsertStaffMemberException(String message, SQLException e){
        super(message);
    }
}
