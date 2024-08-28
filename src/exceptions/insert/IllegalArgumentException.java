package exceptions.insert;

import java.sql.SQLException;

public class IllegalArgumentException extends Exception{
    public IllegalArgumentException(String message, SQLException e){
        super(message);
    }

    public IllegalArgumentException(String message) {
        super(message);
    }
}
