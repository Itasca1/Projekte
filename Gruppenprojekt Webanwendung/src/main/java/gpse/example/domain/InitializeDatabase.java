package gpse.example.domain;

import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Employee;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.FacultyManager;
import gpse.example.domain.entities.GroupManager;
import gpse.example.domain.entities.HTML;
import gpse.example.domain.entities.Review;
import gpse.example.domain.entities.User;
import gpse.example.domain.services.AccessCourseService;
import gpse.example.domain.services.AccessGroupService;
import gpse.example.domain.services.ConfirmationTokenService;
import gpse.example.domain.services.EmployeeService;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.domain.services.FacultyService;
import gpse.example.domain.services.GroupManagerService;
import gpse.example.domain.services.HTMLService;
import gpse.example.domain.services.NotFoundException;
import gpse.example.domain.services.ReviewService;
import gpse.example.domain.services.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * initialize Database.
 */
@Service
@SuppressWarnings({"PMD", "UnusedAssignment"})
public class InitializeDatabase implements InitializingBean {

    private static final String EXAMPLE_PASSWORD = "{bcrypt}$2a$04$WOdXfZQB2FUVEkfsgWePzuBp03V4DS0iAqoe4At1.fI/gY7FR6Ih.";
    private static final String EXAMPLE_SEMESTER = "SS: 2022";

    private static final String EXAMPLE_FACULTY = "Technische Fakultät";
    private static final String EXAMPLE_FACULTYTWO = "Biologie";
    private static final String EXAMPLE_FACULTYTHREE = "Mathematik";

    private static final String EXAMPLE_GROUP = "Algorithmen & Datenstrukturen";
    private static final String EXAMPLE_GROUPTWO = "Genomforschung I";
    private static final String EXAMPLE_GROUPTHREE = "Mathematik für Naturwissenschaften II";

    private static final String EXAMPLE_COURSE = "Algorithmen & Datenstrukturen (V)";
    private static final String EXAMPLE_COURSETWO = "Genomforschung Praktikum";
    private static final String EXAMPLE_COURSETHREE = "Linux Praktikum";


    private static final String EXAMPLE_HTML = "TestHtml";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final UserService userService;
    private final FacultyService facultyService;
    private final AccessCourseService courseService;
    private final AccessGroupService groupService;
    private final EmployeeService employeeService;
    private final GroupManagerService groupManagerService;
    private final FacultyManagerService facultyManagerService;
    private final ReviewService reviewService;
    private final HTMLService htmlService;

    /**
     * Constructor.
     * @param userService for accessing functions for users
     * @param facultyService for accessing functions for faculties
     * @param groupService for accessing functions for groups
     * @param courseService for accessing functions for courses
     * @param employeeService for accessing functions for employees
     * @param groupManagerService for accessing functions for groupManagers
     * @param facultyManagerService for accessing functions for facultyManagers
     * @param reviewService for accessing functions for reviews
     * @param htmlService for accessing functions for HTMLs
     * @param confirmationTokenService for accessing functions for confirmationTokens
     */
    @Autowired
    @SuppressWarnings("checkstyle:ParameterNumber")
    public InitializeDatabase(final UserService userService, final FacultyService facultyService,
                              final AccessGroupService groupService, final AccessCourseService courseService,
                              final EmployeeService employeeService, final GroupManagerService groupManagerService,
                              final FacultyManagerService facultyManagerService, final ReviewService reviewService,
                              final HTMLService htmlService, final ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.facultyService = facultyService;
        this.courseService = courseService;
        this.groupService = groupService;
        this.employeeService = employeeService;
        this.groupManagerService = groupManagerService;
        this.facultyManagerService = facultyManagerService;
        this.reviewService = reviewService;
        this.htmlService = htmlService;
    }

    @Override
    public void afterPropertiesSet() {
        //Admin
        User admin = new User("admin", "Test", "Admin", new ArrayList<>(),
            EXAMPLE_PASSWORD, false, false);
        //New users will have their account locked until verified per Email, so user is here manually enabled
        admin.setUserEnabled(true);

        //Christin S. GroupManager
        User user = new User("christin-s@uni-bielefeld.de", "Christin", "S.", new ArrayList<>(),
             EXAMPLE_PASSWORD, false , false);
        //New users will have their account locked until verified per Email, so user is here manually enabled
        user.setUserEnabled(true);

        //Dustin M. Employee
        User userTwo = new User("dustin-m@uni-bielefeld.de", "Dustin", "M.", new ArrayList<>(),
             EXAMPLE_PASSWORD, false, false);
        //New users will have their account locked until verified per Email, so userTwo is here manually enabled
        userTwo.setUserEnabled(true);

        //Thomas H. FacultyManager
        User userThree = new User("thomas-h@uni-bielefeld.de", "Thomas", "H.", new ArrayList<>(),
             EXAMPLE_PASSWORD, false, false);
        //New users will have their account locked until verified per Email, so userTwo is here manually enabled
        userThree.setUserEnabled(true);




        //Technische Fakultät
        Faculty faculty = new Faculty(EXAMPLE_FACULTY);
        //Biologie
        Faculty facultyTwo = new Faculty(EXAMPLE_FACULTYTWO);
        //Mathematik
        Faculty facultyThree = new Faculty(EXAMPLE_FACULTYTHREE);

        //A&D
        AccessGroup accessGroup = new AccessGroup(EXAMPLE_GROUP, EXAMPLE_SEMESTER, null);
        //Genomforschung I
        AccessGroup accessGroupTwo = new AccessGroup(EXAMPLE_GROUPTWO, EXAMPLE_SEMESTER, null);
        //Mathematik für Naturwissenschaften II
        AccessGroup accessGroupThree = new AccessGroup(EXAMPLE_GROUPTHREE, EXAMPLE_SEMESTER, null);

        AccessCourse accessCourse = new AccessCourse(EXAMPLE_COURSE, null, true, true);
        AccessCourse accessCourseTwo = new AccessCourse(EXAMPLE_COURSETWO, null, true, true);
        AccessCourse accessCourseThree = new AccessCourse(EXAMPLE_COURSETHREE, null, true, true);

        Review reviewOne = new Review();
        reviewOne.setReviewComplete(true);
        Review reviewTwo = new Review();
        reviewTwo.setReviewComplete(true);
        //HTML htmlOne = new HTML(EXAMPLE_HTML, reviewOne, "htmlOne.html");
        //HTML htmlTwo = new HTML(EXAMPLE_HTML, reviewTwo, "htmlTwo.html");

        admin.addRole(ROLE_ADMIN);
        admin.addRole(ROLE_USER);
        user.addRole(ROLE_USER);
        userTwo.addRole(ROLE_USER);
        userThree.addRole(ROLE_USER);

        //User
        try {
            admin = userService.loadUserByMail(admin.getEmail());
        } catch (UsernameNotFoundException e) {
            userService.saveUser(admin);
        }
        try {
            user = userService.loadUserByMail(user.getEmail());
        } catch (UsernameNotFoundException e) {
            userService.saveUser(user);
        }
        try {
            userTwo = userService.loadUserByMail(userTwo.getEmail());
        } catch (UsernameNotFoundException e) {
            userService.saveUser(userTwo);
        }
        try {
            userThree = userService.loadUserByMail(userThree.getEmail());
        } catch (UsernameNotFoundException e) {
            userService.saveUser(userThree);
        }


        //Faculties
        try {
            faculty = facultyService.getFacultyByName(faculty.getName());
        } catch (NotFoundException e) {
            facultyService.saveFaculty(faculty);
        }
        try {
            facultyTwo = facultyService.getFacultyByName(facultyTwo.getName());
        } catch (NotFoundException e) {
            facultyService.saveFaculty(facultyTwo);
        }
        try {
            facultyThree = facultyService.getFacultyByName(facultyThree.getName());
        } catch (NotFoundException e) {
            facultyService.saveFaculty(facultyThree);
        }

        // Groups
        try {
            if (accessGroup.getFaculty() == null) {
                accessGroup.setFaculty(faculty);
            }
            accessGroup = groupService.getGroupByName(accessGroup.getName());
        } catch (NotFoundException e) {
            groupService.saveAccessGroup(accessGroup);
        }

        try {
            if (accessGroupTwo.getFaculty() == null) {
                accessGroupTwo.setFaculty(facultyTwo);
            }
            accessGroupTwo = groupService.getGroupByName(accessGroupTwo.getName());
        } catch (NotFoundException e) {
            groupService.saveAccessGroup(accessGroupTwo);
        }

        try {
            if (accessGroupThree.getFaculty() == null) {
                accessGroupThree.setFaculty(facultyThree);
            }
            accessGroupThree = groupService.getGroupByName(accessGroupThree.getName());
        } catch (NotFoundException e) {
            groupService.saveAccessGroup(accessGroupThree);
        }


        // Courses
        try {
            if (accessCourse.getGroup() == null) {
                accessCourse.setGroup(accessGroup);
            }
            accessCourse = courseService.getCourseByName(accessCourse.getName());
        } catch (NotFoundException e) {
            courseService.saveAccessCourse(accessCourse);
        }

        try {
            if (accessCourseTwo.getGroup() == null) {
                accessCourseTwo.setGroup(accessGroupTwo);
            }
            accessCourseTwo = courseService.getCourseByName(accessCourseTwo.getName());
        } catch (NotFoundException e) {
            courseService.saveAccessCourse(accessCourseTwo);
        }

        try {
            if (accessCourseThree.getGroup() == null) {
                accessCourseThree.setGroup(accessGroup);
            }
            accessCourseThree = courseService.getCourseByName(accessCourseThree.getName());
        } catch (NotFoundException e) {
            courseService.saveAccessCourse(accessCourseThree);
        }


        // Manager

        final List<FacultyManager> facultyManagerByUser = facultyManagerService.getFacultyManagersByUser(userThree);
        if (facultyManagerByUser.isEmpty()) {
            final FacultyManager facultyManager = new FacultyManager(userThree, faculty);
            //final FacultyManager facultyManagerTwo = new FacultyManager(userThree, facultyTwo);
            facultyManagerService.saveFacultyManager(facultyManager);
            //facultyManagerService.saveFacultyManager(facultyManagerTwo);
        }

        final List<GroupManager> groupManagerByUser = groupManagerService.getGroupManagerByUser(user);
        if (groupManagerByUser.isEmpty()) {
            final GroupManager groupManager = new GroupManager(user, accessGroup);
            groupManagerService.saveGroupManager(groupManager);
        }

        //addMenuTestCourses(userTwo, accessGroup);
    }

    // Test Courses used for the "recently visited courses" tab in the menu
    @SuppressWarnings("checkstyle:magicnumber")
    private void addMenuTestCourses(final User menuUser, final AccessGroup group) {
        final ArrayList<AccessCourse> userTwoRecentlyVisited = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            AccessCourse menuAccessCourse = new AccessCourse("Menu Test Kurs " + i, group, true, true);
            try {
                menuAccessCourse = courseService.getCourseByName(menuAccessCourse.getName());
            } catch (NotFoundException e) {
                courseService.saveAccessCourse(menuAccessCourse);
            }
            userTwoRecentlyVisited.add(menuAccessCourse);
            final List<Employee> employeeList = employeeService.getEmployeesByUser(menuUser);
            boolean isAlreadyEmployee = false;
            for (final Employee employee : employeeList) {
                if (employee.getAccessCourse().getId().equals(menuAccessCourse.getId())) {
                    isAlreadyEmployee = true;
                    break;
                }
            }
            if (!isAlreadyEmployee) {
                employeeService.saveEmployee(new Employee(menuUser, menuAccessCourse));
            }
        }
        menuUser.setRecentlyVisitedCourses(userTwoRecentlyVisited);
        userService.updateUser(menuUser.getId().toString(), menuUser);
    }
}
