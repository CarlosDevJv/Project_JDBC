package jdbc.database;

import java.io.Serial;

public class DBIntegrityExcption extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public DBIntegrityExcption(String msg){
        super(msg);
    }
}
