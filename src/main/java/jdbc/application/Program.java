package jdbc.application;

import jdbc.model.dao.DaoFactory;
import jdbc.model.dao.SellerDao;
import jdbc.model.entities.Department;
import jdbc.model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(1);
        System.out.println(seller);

    }
}
