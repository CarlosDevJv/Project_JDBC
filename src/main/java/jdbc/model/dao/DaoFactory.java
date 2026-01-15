package jdbc.model.dao;

import jdbc.database.Db;
import jdbc.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(Db.getConnection() );
    }
}
