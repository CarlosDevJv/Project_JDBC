package jdbc.application;

import jdbc.model.dao.DaoFactory;
import jdbc.model.dao.SellerDao;
import jdbc.model.entities.Department;
import jdbc.model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();
        List<Seller> sellers = sellerDao.findAll();
        for(Seller seller : sellers){
            System.out.println(seller);
        };

    }
}
