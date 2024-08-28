package exceptions.insert;

import java.sql.SQLException;

public class InsertReplacementException extends Exception{
    public InsertReplacementException(String message, SQLException e){
        super(message);
    }
}
