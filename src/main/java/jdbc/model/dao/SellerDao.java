package jdbc.model.dao;

import jdbc.model.entities.Seller;

import java.util.List;

public interface SellerDao {
    void insert(Seller seller);
    void update(Seller seller);
    void Delete(Seller seller);
    Seller findById(Integer id);
    List<Seller> findAll();
}
