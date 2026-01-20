package jdbc.model.dao.impl;

import jdbc.database.DB_Exception;
import jdbc.database.Db;
import jdbc.model.dao.DepartmentDao;
import jdbc.model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    Connection connection = null;

    public DepartmentDaoJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO department (Name) VALUE (?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, department.getName());
            int n = preparedStatement.executeUpdate();

            if(n > 0){
                resultSet = preparedStatement.getGeneratedKeys();

                while (resultSet.next()){
                    int id = resultSet.getInt(1);
                    System.out.println("INSERT DONE WITH SUCCESS! ID = " + id);
                }
            }
        }
        catch (SQLException sqlException){
            throw new DB_Exception("ERROR! " + sqlException.getMessage());
        }
        finally {
            Db.closeResultSet(resultSet);
            Db.closeStatement(preparedStatement);
            Db.closeConnection();
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE seller SET Name = ? WHERE id = ?");
            preparedStatement.setString(1, department.getName());
            preparedStatement.setInt(2, department.getId());
            int n = preparedStatement.executeUpdate();

            if (n > 0){
                System.out.println("UPDATE DONE WITH SUCCESS!");
            }
        }
        catch (SQLException sqlException){
            throw new DB_Exception("ERROR! " + sqlException.getMessage());
        }
        finally {
            Db.closeStatement(preparedStatement);
            Db.closeConnection();
        }
    }

    @Override
    public void Delete(Department department) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("DELETE department WHERE id = ?");
            preparedStatement.setInt(1, department.getId());
            int n = preparedStatement.executeUpdate();

            if (n > 0){
                System.out.println("DELETE DONE WITH SUCCESS!");
            }
        } catch (SQLException e) {
            throw new DB_Exception("ERROR! " + e.getMessage());
        }
        finally {
            Db.closeStatement(preparedStatement);
            Db.closeConnection();
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM department WHERE Id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return implementDepartment(resultSet);
            }
        }
        catch (SQLException e) {
            throw new DB_Exception("ERROR! " + e.getMessage());
        }
        finally {
            Db.closeResultSet(resultSet);
            Db.closeStatement(preparedStatement);
            Db.closeConnection();
        }

        return null;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            List<Department> departmentList  = new ArrayList<>();
            preparedStatement = connection.prepareStatement("SELECT * FROM department");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                Department department = implementDepartment(resultSet);
                departmentList.add(department);
            }
            return departmentList;
        }
        catch (SQLException sqlException){
            throw  new DB_Exception("ERROR! " + sqlException.getMessage());
        }
        finally {
            Db.closeResultSet(resultSet);
            Db.closeStatement(preparedStatement);
            Db.closeConnection();
        }
    }

    private Department implementDepartment(ResultSet resultSet) throws SQLException{
        Department department = new Department();
        department.setId(resultSet.getInt("Id"));
        department.setName(resultSet.getString("Name"));
        return department;
    }
}
