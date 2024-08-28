package exceptions.insert;

import java.sql.SQLException;

public class InsertClientException extends Exception{
    public InsertClientException(String message, SQLException e){
        super(message);
    }
}
