package exceptions.insert;

import java.sql.SQLException;

public class InsertWarningException extends Exception{
    public InsertWarningException(String message, SQLException e){
        super(message);
    }
}
