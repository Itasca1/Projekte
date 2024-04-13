package gpse.example.domain.serviceimpl;


import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.Employee;
import gpse.example.domain.entities.User;
import gpse.example.domain.repositories.EmployeeRepository;
import gpse.example.domain.services.EmployeeService;
import gpse.example.web.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link EmployeeService} needed to load employees.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getEmployees() {
        final List<Employee> employees = new ArrayList<>();
        employeeRepository.findAll().forEach(employees::add);
        return employees;
    }

    @Override
    public Employee loadEmployeeById(final Long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("Employee with id " + id + " not found."));
    }

    @Override
    public Employee saveEmployee(final Employee employee) {
        return employeeRepository.save(employee);
    }


    @Override
    public Employee createEmployee(final User user, final AccessCourse accessCourse) {
        final Employee employee = new Employee(user, accessCourse);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(final String id, final Employee tempEmployee) {
        final Long employeeID = Long.valueOf(id);
        final Employee employee = employeeRepository.findById(employeeID)
            .orElseThrow(BadRequestException::new);
        employee.setUser(tempEmployee.getUser());
        employee.setAccessCourse(tempEmployee.getAccessCourse());
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(final Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public Optional<Employee> getEmployee(final String id) {
        final Long employeeId = Long.valueOf(id);
        return employeeRepository.findById(employeeId);
    }

    @Override
    public List<Employee> getEmployeesByAccessCourse(final AccessCourse accessCourse) {
        final List<Employee> employees = new ArrayList<>();
        final List<Employee> accessCourseEmployees = new ArrayList<>();
        employeeRepository.findAll().forEach(employees::add);
        for (final Employee employee:employees) {
            if (employee.getAccessCourse() == accessCourse) {
                accessCourseEmployees.add(employee);
            }
        }
        return accessCourseEmployees;
    }

    @Override
    public List<Employee> getEmployeesByUser(final User user) {
        final List<Employee> employees = new ArrayList<>();
        final List<Employee> accessCourseEmployees = new ArrayList<>();
        employeeRepository.findAll().forEach(employees::add);
        boolean userfound = false;
        for (final Employee employee:employees) {
            if (employee.getUser().getId().equals(user.getId())) {
                accessCourseEmployees.add(employee);
                userfound = true;
            }
        }
        if (userfound) {
            return accessCourseEmployees;
        } else {
            return new ArrayList<>();
            //throw new UsernameNotFoundException("no Employee found");
        }
    }
    @Override
    public Employee searchForEmployee(final User user, final AccessCourse course) {
        try {
            final List<Employee> employeeList = getEmployeesByUser(user);
            for (final Employee employee : employeeList
            ) {
                if (Objects.equals(employee.getAccessCourse().getName(), course.getName())) {
                    return employee;
                }
            }
            return null;
        } catch (UsernameNotFoundException e) {
            return null;
        }

    }
}

