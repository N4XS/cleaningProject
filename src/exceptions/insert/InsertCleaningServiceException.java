package exceptions.insert;

import java.sql.SQLException;

public class InsertCleaningServiceException extends Exception{
    public InsertCleaningServiceException(String message, SQLException e){
        super(message);
    }
}
