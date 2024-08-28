package exceptions.insert;

import java.sql.SQLException;

public class InsertProductException extends Exception{
    public InsertProductException(String message, SQLException e){
        super(message);
    }
}
