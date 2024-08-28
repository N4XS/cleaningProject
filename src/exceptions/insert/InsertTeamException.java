package exceptions.insert;

import java.sql.SQLException;

public class InsertTeamException extends Exception{
    public InsertTeamException(String message, SQLException e) {
        super(message);
    }
}
