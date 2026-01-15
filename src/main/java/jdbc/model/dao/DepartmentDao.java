package jdbc.model.dao;

import jdbc.model.entities.Department;
import java.util.List;

public interface DepartmentDao {
    void insert(Department department);
    void update(Department department);
    void Delete(Department department);
    Department findById(Integer id);
    List<Department> findAll();
}
