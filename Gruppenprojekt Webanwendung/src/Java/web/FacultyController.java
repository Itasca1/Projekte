package gpse.example.web;

import gpse.example.domain.cmds.FacultyCmd;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

/**
 * Controller for the faculties.
 */
@SuppressWarnings({"ClassCanBeRecord", "PMD.ExcessiveImports"})
@RestController
@CrossOrigin
@RequestMapping("/api")
public class FacultyController {
    private static final String AUTHORIZATION_FAILED = "You are not authorized";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private final FacultyService facultyService;
    private final AccessGroupService groupService;
    private final AccessCourseService courseService;
    private final UserService userService;
    private final FacultyManagerService facultyManagerService;
    private final GroupManagerService groupManagerService;
    private final EmployeeService employeeService;

    /**
     * FacultyController.
     *
     * @param facultyService        FacultyService
     * @param groupService          GroupService
     * @param courseService         CourseService
     * @param userService           UserService
     * @param facultyManagerService FacultyManagerService
     * @param groupManagerService   GroupManagerService
     * @param employeeService       EmployeeService
     */
    @Autowired
    public FacultyController(final FacultyService facultyService, final AccessGroupService groupService,
                             final AccessCourseService courseService, final UserService userService,
                             final FacultyManagerService facultyManagerService,
                             final GroupManagerService groupManagerService,
                             final EmployeeService employeeService) {
        this.facultyService = facultyService;
        this.groupService = groupService;
        this.courseService = courseService;
        this.userService = userService;
        this.facultyManagerService = facultyManagerService;
        this.groupManagerService = groupManagerService;
        this.employeeService = employeeService;
    }

    /**
     * Get all Faculties.
     *
     * @return all Faculties
     */
    @GetMapping("/allfaculties")
    public List<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    /**
     * Returns Faculties for a User via GET-Request.
     *
     * @return myfaculties
     */
    @GetMapping("/faculties")
    @Secured(ROLE_USER)
    public List<Faculty> getFaculties() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final List<FacultyManager> userfaculties = facultyManagerService.getFacultyManagersByUser((User) author);
        final List<Faculty> myFaculties = new ArrayList<>();
        for (final FacultyManager manager : userfaculties) {
            myFaculties.add(manager.getFaculty());
        }
        return myFaculties;
    }

    /**
     * Returns specific faculty for the api.
     *
     * @param id the id of the faculty
     * @return the faculty
     */
    @GetMapping("/faculties/{id:\\d+}")
    @Secured(ROLE_USER)
    public Faculty getFaculty(@PathVariable("id") final Long id) throws NoAuthorizationException {
        Faculty faculty;
        if (facultyService.getFacultyById(id).isPresent()) {
            faculty = facultyService.getFacultyById(id).get();
        } else {
            throw new BadRequestException();
        }
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());

        if (userInFaculty((User) author, faculty, facultyManagerService, groupManagerService, employeeService,
                groupService)) {
            return faculty;
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }

    /**
     * Returns true if user is part of given faculty.
     * Checks: faculty-manager, group-manager of faculty, employees of groups of faculty.
     * @param user user to check
     * @param faculty faculty where the user should be in
     * @param facultyManagerService the facultyManagerService
     * @param groupManagerService the groupManagerService
     * @param employeeService the employeeService
     * @param groupService the groupService
     * @return true if user is in faculty, otherwise false.
     */
    public static boolean userInFaculty(final User user, final Faculty faculty,
                                        final FacultyManagerService facultyManagerService,
                                        final GroupManagerService groupManagerService,
                                        final EmployeeService employeeService,
                                        final AccessGroupService groupService) {
        try {
            final FacultyManager facultyManager = facultyManagerService.searchForFacultyManager(user, faculty);
            if (facultyManager != null) {
                return true;
            }
        } catch (UsernameNotFoundException e) {
            // idk
        }

        try {
            for (final GroupManager groupManager : groupManagerService.getGroupManagerByUser(user)) {
                if (groupManager.getAccessGroup().getFaculty().getId().equals(faculty.getId())) {
                    return true;
                }
            }
        } catch (UsernameNotFoundException e) {
            // hmm
        }

        try {
            final List<AccessGroup> groupsOfFaculty = groupService.getGroupsByFaculty(faculty);
            for (final Employee employee : employeeService.getEmployeesByUser(user)) {
                if (groupsOfFaculty.contains(employee.getAccessCourse().getGroup())) {
                    return true;
                }
            }
        } catch (UsernameNotFoundException e) {
            return false;
        }

        return false;
    }

    /**
     * Creates a new faculty via POST-Request.
     *
     * @param facultyCmd The faculty
     * @return created faculty
     */
    @PostMapping("/faculties")
    @Secured(ROLE_ADMIN)
    public Faculty store(@RequestBody final FacultyCmd facultyCmd) {
        return facultyService.createFaculty(facultyCmd.getName());
    }

    /**
     * Updates a faculty via PUT-Request.
     *
     * @param id         the id of the faculty
     * @param facultyCmd the FacultyCmd object containing updated information
     * @return updated faculty
     */
    @PutMapping("/faculties/{id:\\d+}")
    @Secured(ROLE_USER)
    public Faculty update(@PathVariable("id") final String id,
                          @RequestBody final FacultyCmd facultyCmd) throws NoAuthorizationException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final List<FacultyManager> userfaculties = facultyManagerService.getFacultyManagersByUser((User) author);
        boolean isManager = false;
        for (final FacultyManager manager : userfaculties) {
            if (manager.getFaculty().getId().equals(Long.valueOf(id))) {
                isManager = true;
            }
        }
        if (isManager) {
            return facultyService.updateFaculty(Long.valueOf(id), facultyCmd);
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }

    }

    /**
     * Calculates the score for a faculty.
     *
     * @param id the id of the faculty
     * @return the score
     */
    @GetMapping("/faculty/{id:\\d+}/score")
    public int getScore(@PathVariable("id") final String id) throws NoAuthorizationException {
        final Faculty faculty = getFaculty(Long.valueOf(id));
        final List<AccessGroup> groupsOfFaculty = groupService.getGroupsByFaculty(faculty);
        int totalScore = 0;
        int courses = 0;
        for (final AccessGroup accessGroup : groupsOfFaculty) {
            final List<AccessCourse> coursesOfGroup = courseService.getCoursesByGroup(accessGroup);
            for (final AccessCourse accessCourse : coursesOfGroup) {
                if (accessCourse.isWeighted()) {
                    totalScore += 2 * accessCourse.getScore();
                    courses += 2;
                } else {
                    totalScore += accessCourse.getScore();
                    courses++;
                }
            }
        }
        if (courses == 0) {
            return 0;
        }
        return Math.round(((float) totalScore) / courses);
    }

    /**
     * Deletes Faculty.
     *
     * @param facultyCmd The faculty
     * @throws NotFoundException Faculty not found
     */
    @PostMapping("/deleteFaculty")
    @Secured(ROLE_ADMIN)
    public void deleteFaculty(@RequestBody final FacultyCmd facultyCmd) throws NotFoundException {
        final Faculty faculty = facultyService.getFacultyByName(facultyCmd.getName());
        facultyService.deleteFaculty(faculty);
    }

    /**
     * Returns true if user is manager of faculty.
     *
     * @param id ID of faculty.
     * @return true if user is manager of faculty, otherwise false.
     */
    @GetMapping("/faculty-edit/{id:\\d+}")
    public boolean allowedToEdit(@PathVariable("id") final String id) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final User user = (User) author;
        if (facultyService.getFacultyById(Long.valueOf(id)).isPresent()) {
            final Faculty faculty = facultyService.getFacultyById(Long.valueOf(id)).get();
            for (final FacultyManager facultyManager : facultyManagerService.getFacultyManagerByFaculty(faculty)) {
                if (facultyManager.getUser().getId().equals(user.getId())) {
                    return true;
                }
            }
            return false;
        } else {
            throw new BadRequestException();
        }
    }
}

