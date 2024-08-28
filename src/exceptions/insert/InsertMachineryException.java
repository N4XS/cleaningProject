package exceptions.insert;

import java.sql.SQLException;

public class InsertMachineryException extends Exception{
    public InsertMachineryException(String message, SQLException e){
        super(message);
    }
}
