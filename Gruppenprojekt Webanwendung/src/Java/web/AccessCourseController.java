package gpse.example.web;

import gpse.example.domain.cmds.AccessCourseCmd;
import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Employee;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.FacultyManager;
import gpse.example.domain.entities.GroupManager;
import gpse.example.domain.entities.User;
import gpse.example.domain.services.AccessCourseService;
import gpse.example.domain.services.AccessGroupService;
import gpse.example.domain.services.EmployeeService;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.domain.services.FacultyService;
import gpse.example.domain.services.GroupManagerService;
import gpse.example.domain.services.NoAuthorizationException;
import gpse.example.domain.services.NotFoundException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller for AccessCourses.
 */
@SuppressWarnings({"ClassCanBeRecord", "PMD.ExcessiveImports"})
@RestController
@CrossOrigin
@RequestMapping("/api")
public class AccessCourseController {
    private static final String AUTHORIZATION_FAILED = "You are not authorized";
    private static final String ROLE_USER = "ROLE_USER";
    private final AccessCourseService courseService;
    private final FacultyManagerService facultyManagerService;
    private final FacultyService facultyService;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final GroupManagerService groupManagerService;
    private final AccessGroupService groupService;

    /**
     * Controller for AccessCourse.
     *
     * @param courseService         courseService
     * @param employeeService       employeeService
     * @param userService           userService
     * @param groupManagerService   groupManagerService
     * @param groupService          groupServic
     * @param facultyService        facultyService
     * @param facultyManagerService facultyManagerService
     */
    @Autowired
    public AccessCourseController(final AccessCourseService courseService,
                                  final EmployeeService employeeService,
                                  final UserService userService,
                                  final GroupManagerService groupManagerService,
                                  final AccessGroupService groupService,
                                  final FacultyService facultyService,
                                  final FacultyManagerService facultyManagerService) {
        this.facultyService = facultyService;
        this.facultyManagerService = facultyManagerService;
        this.courseService = courseService;
        this.userService = userService;
        this.employeeService = employeeService;
        this.groupManagerService = groupManagerService;
        this.groupService = groupService;
    }


    /**
     * Loads all courses that are in a group.
     * This group is represented by its id.
     *
     * @param groupId id of the group
     * @return all courses of the group
     */
    @GetMapping("/group-courses/{id:\\d+}")
    @Secured(ROLE_USER)
    public List<AccessCourse> getCoursesByGroupId(@PathVariable("id") final String groupId) {
        final List<AccessCourse> allCourses = new ArrayList<>();

        for (final AccessCourse course : courseService.getCourses()) {
            if (Objects.equals(course.getGroup().getId(), Long.valueOf(groupId))) {
                allCourses.add(course);
            }
        }

        return allCourses;
    }

    @GetMapping("/visible-courses")
    @Secured(ROLE_USER)
    public List<AccessCourse> getCoursesByVisibility() {
        return courseService.getVisibleCourses();
    }



    /**
     * Returns Courses for a User via GET-Request.
     *
     * @return myCourses
     */
    @GetMapping("/courses")
    @Secured(ROLE_USER)
    public List<AccessCourse> getCourses() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final List<Employee> usercourses = employeeService.getEmployeesByUser((User) author);
        final List<AccessCourse> myCourses = new ArrayList<>();
        for (final Employee employee : usercourses) {
            myCourses.add(employee.getAccessCourse());
        }
        return myCourses;
    }

    /**
     * Calculates the average score over all courses of the user.
     *
     * @return average score
     */
    @GetMapping("/avgMyCourses")
    public int getAvgMyCourses() {
        final List<AccessCourse> myCourses = getCourses();
        int sum = 0;
        for (final AccessCourse currentCourse : myCourses) {
            sum += currentCourse.getScore();
        }
        return Math.round(((float) sum) / myCourses.size());
    }

    @GetMapping("/allcourses")
    //@Secured(ROLE_USER)
    public List<AccessCourse> getAllCourses() {
        return courseService.getCourses();
    }

    /**
     * Returns specific course for the api.
     *
     * @param id the id of the course
     * @return the course
     */
    @GetMapping("/courses/{id:\\d+}")
    @Secured(ROLE_USER)
    public AccessCourse getCourse(@PathVariable("id") final String id) throws NoAuthorizationException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());

        if (courseService.getCourse(id).isPresent()) {
            final AccessCourse course = courseService.getCourse(id).get();
            final AccessGroup group = course.getGroup();
            final Faculty faculty = group.getFaculty();
            if (FacultyController.userInFaculty((User) author, faculty, facultyManagerService, groupManagerService,
                    employeeService, groupService)) {
                return courseService.getCourse(id).get();
            } else {
                throw new NoAuthorizationException(AUTHORIZATION_FAILED);
            }
        } else {
            throw new BadRequestException();
        }
    }

    /**
     * Creates a new AccessCourse via POST-Request.
     *
     * @param courseCmd CourseCmd
     * @return created AccessCourse
     */
    @PostMapping("/courses")
    @Secured(ROLE_USER)
    public AccessCourse store(@RequestBody final AccessCourseCmd courseCmd)
            throws NoAuthorizationException, NotFoundException {
        final Optional<AccessGroup> possibleGroup = groupService.getGroupById(courseCmd.getGroupID());
        if (possibleGroup.isEmpty()) {
            throw new NotFoundException("Group not found");
        }
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final List<GroupManager> userGroups = groupManagerService.getGroupManagerByUser((User) author);
        final List<FacultyManager> userFaculties = facultyManagerService.getFacultyManagersByUser((User) author);
        final AccessGroup storeGroup = possibleGroup.get();
        final Faculty storeFac = facultyService.getFacultyByName(storeGroup.getFaculty().getName());
        boolean isGroupManager = false;
        // when users manage the group where we want to save the course
        for (final GroupManager groupManager : userGroups) {
            if (Objects.equals(groupManager.getAccessGroup().getId(), storeGroup.getId())) {
                isGroupManager = true;
                break;
            }
        }
        // when users manage the faculty where we want to save the course
        for (final FacultyManager facultyManager : userFaculties) {
            if (Objects.equals(facultyManager.getFaculty().getId(), storeFac.getId())) {
                isGroupManager = true;
                break;
            }
        }
        if (isGroupManager) {
            final AccessCourse newCourse =
                    courseService.createAccessCourse(courseCmd.getName(), storeGroup,
                            courseCmd.isVisible(), courseCmd.isWeighted());
            employeeService.createEmployee((User) author, newCourse);
            return newCourse;
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }


    /**
     * Updates a AccessCourse via PUT-Request.
     *
     * @param id        ID of Updated Course
     * @param courseCmd CourseCmd
     * @return updated AccessCourse
     */
    @PutMapping("/courses/{id:\\d+}")
    @Secured(ROLE_USER)
    public AccessCourse update(@PathVariable("id") final String id, @RequestBody final AccessCourseCmd courseCmd)
            throws NoAuthorizationException {
        if (manageAbove(id)) {
            final AccessCourse course = new AccessCourse(courseCmd.getName(), courseCmd.getGroup(),
                    courseCmd.isVisible(), courseCmd.isWeighted());
            course.setHtmlList(courseCmd.getHtmlList());
                course.setCoursePublic(courseCmd.isCoursePublic());
            return courseService.updateAccessCourse(id, course);
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }

    @GetMapping("/course/{id:\\d+}/score")
    public int getScore(@PathVariable("id") final String id) throws NoAuthorizationException {
        final AccessCourse course = getCourse(id);
        return course.getScore();
    }

    /**
     * Deletes a AccessCourse via PUT-Request.
     *
     * @param id ID of deleted AccessCourse
     * @return updated AccessCourse
     */
    @PutMapping("/course/{id:\\d+}")
    @Secured(ROLE_USER)
    public AccessCourse delete(@PathVariable("id") final String id)
            throws NoAuthorizationException {
        if (manageAbove(id)) {
            AccessCourse course;
            if (courseService.getCourse(id).isPresent()) {
                course = courseService.getCourse(id).get();
                return courseService.deleteAccessCourse(course);
            } else {
                throw new BadRequestException();
            }
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }

    /**
     * if user manager above Course => true, else => false.
     *
     * @param id the id of the course
     * @return wether the current user is manager or employee for the course
     */
    public boolean manageAbove(final String id) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        List<Employee> usercourses;
        usercourses = employeeService.getEmployeesByUser((User) author);
        List<GroupManager> userGroups;
        userGroups = groupManagerService.getGroupManagerByUser((User) author);
        List<FacultyManager> userFaculties;
        userFaculties = facultyManagerService.getFacultyManagersByUser((User) author);
        if (courseService.getCourse(id).isEmpty()) {
            throw new BadRequestException();
        }
        final AccessCourse course = courseService.getCourse(id).get();
        final AccessGroup group = course.getGroup();
        final Faculty faculty = group.getFaculty();
        for (final FacultyManager facultyManager : userFaculties) {
            if (facultyManager.getFaculty().getId().equals(faculty.getId())) {
                return true;
            }
        }
        for (final GroupManager groupManager : userGroups) {
            if (groupManager.getAccessGroup().getId().equals(group.getId())) {
                return true;
            }
        }
        for (final Employee employee : usercourses) {
            if (employee.getAccessCourse().getId().equals(course.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if user is allowed to edit an AccessCourse.
     *
     * @param id ID of AccessGroup.
     * @return true if user manages group or manages faculty of group, otherwiese false.
     */
    @GetMapping("/course-edit/{id:\\d+}")
    @Secured(ROLE_USER)
    public boolean allowedToEdit(@PathVariable("id") final String id) {
        return manageAbove(id);

    }

}

