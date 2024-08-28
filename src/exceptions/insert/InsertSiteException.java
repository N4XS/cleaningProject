package exceptions.insert;

import java.sql.SQLException;

public class InsertSiteException extends Exception{
    public InsertSiteException(String message, SQLException e){
        super(message);
    }

}
