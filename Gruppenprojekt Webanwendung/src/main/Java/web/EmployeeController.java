package gpse.example.web;

import gpse.example.domain.cmds.EmployeeCmd;
import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Employee;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.User;
import gpse.example.domain.services.AccessCourseService;
import gpse.example.domain.services.EmployeeService;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.domain.services.GroupManagerService;
import gpse.example.domain.services.NoAuthorizationException;
import gpse.example.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static gpse.example.web.ManagerHelper.isEmployee;
import static gpse.example.web.ManagerHelper.isFacultyManager;
import static gpse.example.web.ManagerHelper.isGroupManager;

/**
 * Controller for Employee.
 */
@SuppressWarnings({"ClassCanBeRecord", "PMD.ExcessiveImports"})
@RestController
@CrossOrigin
@RequestMapping("/api")
public class EmployeeController {
    private static final String AUTHORIZATION_FAILED = "You are not authorized";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private final EmployeeService employeeService;
    private final UserService userService;
    private final GroupManagerService groupManagerService;
    private final AccessCourseService accessCourseService;
    private final FacultyManagerService facultyManagerService;

    /**
     * Constructor.
     *
     * @param employeeService the employeeService
     * @param userService the userService
     * @param groupManagerService the groupManagerService
     * @param accessCourseService the courseService
     * @param facultyManagerService the facultyManagerService
     */
    @Autowired
    public EmployeeController(final EmployeeService employeeService, final UserService userService,
                              final GroupManagerService groupManagerService,
                              final AccessCourseService accessCourseService,
                              final FacultyManagerService facultyManagerService) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.groupManagerService = groupManagerService;
        this.accessCourseService = accessCourseService;
        this.facultyManagerService = facultyManagerService;
    }

    /**
     * Returns all Employees (as list of User object) of a given AccessCourse.
     *
     * @param courseID ID of given AccessCourse
     * @return employees of AccessCourse
     */
    @GetMapping("/course-employees/{course-id:\\d+}")
    @Secured(ROLE_USER)
    public List<User> getEmployeeFromCourse(@PathVariable("course-id") final String courseID) {
        AccessCourse course;
        if (accessCourseService.getCourse(courseID).isPresent()) {
            course = accessCourseService.getCourse(courseID).get();
        } else {
            return new ArrayList<>();
        }
        final List<User> users = new ArrayList<>();
        final List<Employee> employees = employeeService.getEmployeesByAccessCourse(course);
        employees.forEach(employee -> users.add(employee.getUser()));
        return users;
    }


    /**
     * Returns a List of employee objects of the current user.
     *
     * @return List of employees of current user
     */
    @GetMapping("getMyEmployees")
    @Secured(ROLE_USER)
    public List<Employee> getMyEmployees() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        return employeeService.getEmployeesByUser((User) author);
    }

    /**
     * Returns a List of group manager objects that can be deleted by the current user.
     *
     * @return List of group manager that can be deleted by current user
     */
    @GetMapping("getMyDeletableEmployees")
    @Secured(ROLE_USER)
    public List<Employee> getMyDeletableGroupEmployees() {
        final List<Employee> myEmployees = getMyEmployees();
        final List<Employee> allEmployees = showEmployees();
        final List<Employee> myDeletableEmployees = new ArrayList<>();
        for (final Employee employee : allEmployees) {
            for (final Employee myEmployee : myEmployees) {
                if (employee.getAccessCourse().getId().equals(myEmployee.getAccessCourse().getId())) {
                    myDeletableEmployees.add(employee);
                }
            }
        }
        return myDeletableEmployees;
    }

    /**
     * Returns employees from user, via GET-Request.
     *
     * @return employees
     */
    @GetMapping("/employees")
   // @Secured(ROLE_USER)
    public List<Employee> showEmployees() {
        return employeeService.getEmployees();
    }

    /**
     * get Employee.
     *
     * @param id pathvariable of Employee
     * @return Employee with id
     */
    @GetMapping("/employees/{id:\\d+}")
    @Secured(ROLE_USER)
    public Employee showEmployee(@PathVariable("id") final String id) {
        if (employeeService.getEmployee(id).isPresent()) {
            return employeeService.getEmployee(id).get();
        } else {
            throw new BadRequestException();
        }
    }

    /**
     * store Employee.
     *
     * @param employeeCmd Commandobject
     * @return create employee
     */
    @PostMapping("/employees")
    @Secured(ROLE_USER)
    public Employee store(@RequestBody final EmployeeCmd employeeCmd) throws NoAuthorizationException {
        if (isManager(employeeCmd.getCourseID())) {
            final Optional<AccessCourse> courseOptional =
                    accessCourseService.getCourse(String.valueOf(employeeCmd.getCourseID()));
            if (courseOptional.isEmpty()) {
                throw new BadRequestException();
            }
            final AccessCourse accessCourse = courseOptional.get();
            final User user = userService.loadUserById(employeeCmd.getUserID());
            final Employee employee = new Employee(user, accessCourse);
            return employeeService.saveEmployee(employee);
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }

    /**
     * delete Employee.
     *
     * @param employeeCmd command object
     * @throws BadRequestException Employee not found
     */
    @PostMapping("/deleteEmployee")
    @Secured({ROLE_ADMIN, ROLE_USER})
    public void deleteEmployee(@RequestBody final EmployeeCmd employeeCmd)
            throws BadRequestException, NoAuthorizationException {
        final User user = userService.loadUserById(employeeCmd.getUserID());
        final AccessCourse course = accessCourseService.getCourse(employeeCmd.getCourseID().toString())
                .orElseThrow(BadRequestException::new);
        Employee oldEmployee;
        oldEmployee = employeeService.searchForEmployee(user, course);
        if (Objects.isNull(oldEmployee)) {
            throw new BadRequestException();
        }
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User author = (User) userService.loadUserByUsername(auth.getName());
        if (author.getRoles().contains(ROLE_ADMIN)) {
            employeeService.deleteEmployee(oldEmployee);
        } else {
            // test whether the user Manages the Course
            final List<Employee> userEmployees = employeeService.getEmployeesByUser(author);
            for (final Employee currentEmployee : userEmployees) {
                if (currentEmployee.getAccessCourse().getId().equals(course.getId())) {
                    employeeService.deleteEmployee(oldEmployee);
                } else {
                    throw new NoAuthorizationException(AUTHORIZATION_FAILED);
                }
            }
        }
    }


    /**
     * Checks wether the current user is manager of a course.
     *
     * @param id the id of the course
     * @return wether the current user is a manager of the course
     */
    public boolean isManager(final Long id) {
        if (accessCourseService.getCourse(String.valueOf(id)).isEmpty()) {
            throw new BadRequestException();
        }
        final AccessCourse course = accessCourseService.getCourse(String.valueOf(id)).get();
        final AccessGroup group = course.getGroup();
        final Faculty faculty = group.getFaculty();
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) userService.loadUserByUsername(auth.getName());
        return isEmployee(user, course, employeeService) || isGroupManager(user, group, groupManagerService)
                || isFacultyManager(user, faculty, facultyManagerService);
    }
}

