package jdbc.model.dao.impl;

import jdbc.database.DB_Exception;
import jdbc.database.Db;
import jdbc.model.dao.SellerDao;
import jdbc.model.entities.Department;
import jdbc.model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
    Connection connection = null;

    public SellerDaoJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                resultSet = preparedStatement.getGeneratedKeys();
                while (resultSet.next()){
                    int id = resultSet.getInt(1);
                    seller.setId(id);
                    System.out.println("INSERT DONE WITH SUCCESS! ID = " + id);
                }
            }
            else {
                throw new DB_Exception("Unexpected error! No rows affected");
            }
        } catch (SQLException e) {
            throw new DB_Exception("ERROR! " + e.getMessage());
        }
        finally {
            Db.closeResultSet(resultSet);
            Db.closeStatement(preparedStatement);
        }

    }

    @Override
    public void update(Seller seller) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());
            preparedStatement.setInt(6, seller.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0){
                resultSet = preparedStatement.getGeneratedKeys();
                while (resultSet.next()){
                    int id = resultSet.getInt(1);
                    System.out.println("UPDATE DONE WITH SUCCESS! ID = " + id);
                }
            }
        }
        catch (SQLException sqlException){
            throw new DB_Exception("ERROR! " + sqlException.getMessage());
        }
        finally {
            Db.closeResultSet(resultSet);
            Db.closeStatement(preparedStatement);
        }

    }

    @Override
    public void Delete(Seller seller) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");
            preparedStatement.setInt(1, seller.getId());
            int n = preparedStatement.executeUpdate();

            if (n > 0){
                System.out.println("DELETE DONE WITH SUCCESS!");
            }
        }
        catch (SQLException sqlException){
            throw new DB_Exception("ERROR! " + sqlException.getMessage());
        }
        finally {
            Db.closeStatement(preparedStatement);
        }

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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT seller.*, department.Name " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY seller.Name");

            resultSet = preparedStatement.executeQuery();
            Map<Integer, Department> departmentMap = new HashMap<>();
            List<Seller> sellers = new ArrayList<>();

            while (resultSet.next()){
                Department department = departmentMap.get(resultSet.getInt("DepartmentId"));

                if (department == null){
                    department = implementDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), department);
                }
                Seller seller = implementSeller(resultSet, department);
                sellers.add(seller);

                }return sellers;
        }
        catch (SQLException sqlException){
            throw new DB_Exception("ERROR! " + sqlException.getMessage());
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT seller.*,department.Name " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.id " +
                    "WHERE department.id = ? " +
                    "ORDER BY seller.Name");
            preparedStatement.setInt(1, department.getId());
            resultSet = preparedStatement.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (resultSet.next()){
                Department dep = departmentMap.get(resultSet.getInt("DepartmentId"));
                if(dep == null){
                    dep = implementDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), dep);
                }
                Seller seller = null;
                seller = implementSeller(resultSet, dep);
                sellers.add(seller);

            } return sellers;
        }
        catch (SQLException sqlException){
            throw new DB_Exception("ERROR! " + sqlException.getMessage());
        }
        finally {
            Db.closeStatement(preparedStatement);
            Db.closeResultSet(resultSet);
        }
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
