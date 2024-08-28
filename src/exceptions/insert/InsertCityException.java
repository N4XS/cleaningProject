package exceptions.insert;

import java.sql.SQLException;

public class InsertCityException extends Exception{
    public InsertCityException(String message, SQLException e){
        super(message);
    }

    public InsertCityException(String message) {
        super(message);
    }
}
