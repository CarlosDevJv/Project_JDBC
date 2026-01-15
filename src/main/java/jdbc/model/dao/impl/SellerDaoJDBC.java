package jdbc.model.dao.impl;

import jdbc.database.DB_Exception;
import jdbc.database.Db;
import jdbc.model.dao.SellerDao;
import jdbc.model.entities.Department;
import jdbc.model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {
    Connection connection = null;

    public SellerDaoJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void Delete(Seller seller) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT seller.*,department.Name " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Department department = implementDepartment(resultSet);
                Seller seller = implementSeller(resultSet, department);
                return seller;

            }
        }
        catch (SQLException sqlException){
            throw new DB_Exception("ERROR! " + sqlException.getMessage());
        }
        finally {
            Db.closeResultSet(resultSet);
            Db.closeStatement(preparedStatement);
        }
        return null;
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }

    private Department implementDepartment(ResultSet resultSet) throws SQLException{
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("department.Name"));
        return department;
    }

    private Seller implementSeller(ResultSet resultSet, Department department)throws SQLException{
        Seller seller = new Seller();
        seller.setName(resultSet.getString("Name"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setId(resultSet.getInt("Id"));
        seller.setDepartment(department);
        return seller;
    }
}
