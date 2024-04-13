package gpse.example.domain.repositories;

import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.Employee;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for Employee.
 */
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    Employee findByAccessCourse(AccessCourse accessCourse);
}

