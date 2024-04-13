package gpse.example;

import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.Employee;
import gpse.example.domain.entities.User;
import gpse.example.domain.repositories.CourseRepository;
import gpse.example.domain.repositories.EmployeeRepository;
import gpse.example.domain.repositories.UserRepository;
import gpse.example.domain.serviceimpl.AccessCourseServiceImpl;
import gpse.example.domain.serviceimpl.EmployeeServiceImpl;
import gpse.example.domain.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeTest {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    /* default */ void employeeCreation() {
        // Create employee
        final EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepository);
        final AccessCourseServiceImpl courseService = new AccessCourseServiceImpl(courseRepository);
        final UserServiceImpl userService = new UserServiceImpl(userRepository, null, null);
        final AccessCourse course = new AccessCourse("employeeCreationTestCourse", null, false,true);
        courseService.saveAccessCourse(course);
        final User user = new User("employeeCreationTestMail", "employeeCreationTestFirstname",
                "employeeCreationTestLastname", new ArrayList<>(),  "employeeCreationTestPassword", false ,false);
        userService.saveUser(user);
        Employee employee = employeeService.createEmployee(user, course);

        // Test
        final Optional<Employee> employeeOpt = employeeService.getEmployee(String.valueOf(employee.getId()));
        assertThat(employeeOpt.isPresent()).isTrue();
        employee = employeeOpt.get();
        assertThat(employee.getId()).isNotNull();
        assertThat(employee.getAccessCourse().getId()).isEqualTo(courseService.getCourses().get(0).getId());
        assertThat(employee.getUser().getId()).isEqualTo(userService.getUsers().get(0).getId());
    }

    @Test
    /* default */ void employeeUpdate() {
        // Create employee
        final EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepository);
        final AccessCourseServiceImpl courseService = new AccessCourseServiceImpl(courseRepository);
        final UserServiceImpl userService = new UserServiceImpl(userRepository, null, null);
        final AccessCourse course = new AccessCourse("employeeUpdateTestCourse", null, false,true);
        courseService.saveAccessCourse(course);
        final User user = new User("employeeCreationUpdateMail", "employeeUpdateTestFirstname",
                "employeeUpdateTestLastname", new ArrayList<>(), "employeeUpdateTestPassword", false ,false);
        userService.saveUser(user);
        Employee employee = employeeService.createEmployee(user, course);

        // Update course
        AccessCourse newCourse = new AccessCourse("employeeUpdatedTestCourse", null, false,true);
        newCourse = courseService.saveAccessCourse(newCourse);
        employee.setAccessCourse(newCourse);
        employee = employeeService.updateEmployee(String.valueOf(employee.getId()), employee);

        // Test
        final Optional<Employee> updatedEmployeeOpt = employeeService.getEmployee(String.valueOf(employee.getId()));
        assertThat(updatedEmployeeOpt.isPresent()).isTrue();
        final Employee updatedEmployee = updatedEmployeeOpt.get();
        assertThat(updatedEmployee.getAccessCourse().getId()).isEqualTo(newCourse.getId());
        assertThat(updatedEmployee.getAccessCourse().getName()).isEqualTo("employeeUpdatedTestCourse");
        assertThat(updatedEmployee.getUser().getId()).isEqualTo(userService.getUsers().get(0).getId());
    }

    @Test
    /* default */ void employeeDelete() {
        // Create employee
        final EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepository);
        final AccessCourseServiceImpl courseService = new AccessCourseServiceImpl(courseRepository);
        final UserServiceImpl userService = new UserServiceImpl(userRepository, null, null);
        final AccessCourse course = new AccessCourse("employeeDeleteTestCourse", null, false,true);
        courseService.saveAccessCourse(course);
        final User user = new User("employeeDeleteUpdateMail", "employeeDeleteTestFirstname",
                "employeeDeleteTestLastname", new ArrayList<>(),  "employeeDeleteTestPassword", false ,false);
        userService.saveUser(user);
        final Employee employee = employeeService.createEmployee(user, course);

        // Delete employee
        employeeService.deleteEmployee(employee);

        // Test
        assertThat(employeeService.getEmployee(String.valueOf(employee.getId()))).isEmpty();
        assertThat(courseService.getCourse(String.valueOf(course.getId()))).isPresent();
        assertThat(userService.getUser(String.valueOf(user.getId()))).isPresent();
    }
}
