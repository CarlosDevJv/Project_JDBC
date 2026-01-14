package jdbc.database;

import java.io.Serial;

public class DB_Exception extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public DB_Exception(String msg){
        super(msg);
    }
}
