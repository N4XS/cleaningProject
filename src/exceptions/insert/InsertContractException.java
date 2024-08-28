package exceptions.insert;

import java.sql.SQLException;

public class InsertContractException extends Exception{
    public InsertContractException(String message, SQLException e){
        super(message);
    }
}
