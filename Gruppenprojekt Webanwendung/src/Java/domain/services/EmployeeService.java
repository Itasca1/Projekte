package gpse.example.domain.services;


import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.Employee;
import gpse.example.domain.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Service for Employee.
 */
public interface EmployeeService {

    List<Employee> getEmployees();

    Employee loadEmployeeById(Long id);

    List<Employee> getEmployeesByAccessCourse(AccessCourse accessCourse);

    Employee saveEmployee(Employee employee);

    Employee createEmployee(User user, AccessCourse accessCourse);

    Employee updateEmployee(String id, Employee employee);

    void deleteEmployee(Employee employee);

    Optional<Employee> getEmployee(String id);

    List<Employee> getEmployeesByUser(User user);

    Employee searchForEmployee(User user, AccessCourse course);
}

