package exceptions.insert;

import java.sql.SQLException;

public class InsertMaterialsOrderException extends Exception{
    public InsertMaterialsOrderException(String message, SQLException e){
        super(message);
    }
}
