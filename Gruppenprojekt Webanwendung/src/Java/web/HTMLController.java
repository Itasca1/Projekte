package gpse.example.web;

import gpse.example.domain.cmds.HTMLCmd;
import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.HTML;
import gpse.example.domain.entities.User;
import gpse.example.domain.services.AccessCourseService;
import gpse.example.domain.services.EmployeeService;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.domain.services.GroupManagerService;
import gpse.example.domain.services.HTMLService;
import gpse.example.domain.services.NoAuthorizationException;
import gpse.example.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static gpse.example.web.ManagerHelper.isEmployee;
import static gpse.example.web.ManagerHelper.isFacultyManager;
import static gpse.example.web.ManagerHelper.isGroupManager;

/**
 * Controller for HTML objects.
 */
@SuppressWarnings({"ClassCanBeRecord", "PMD.ExcessiveImports"})
@RestController
@CrossOrigin
@RequestMapping("/api")
public class HTMLController {
    private static final String AUTHORIZATION_FAILED = "You are not authorized";
    private static final String ROLE_USER = "ROLE_USER";
    private final HTMLService htmlService;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final AccessCourseService accessCourseService;
    private final GroupManagerService groupManagerService;
    private final FacultyManagerService facultyManagerService;

    /**
     * Constructor.
     *
     * @param htmlService the htmlService
     * @param userService the userService
     * @param employeeService the employeeService
     * @param accessCourseService the courseService
     * @param groupManagerService the the groupManagerService
     * @param facultyManagerService the facultyManagerService
     */
    @Autowired
    public HTMLController(final HTMLService htmlService, final UserService userService,
                          final EmployeeService employeeService,
                          final AccessCourseService accessCourseService, final GroupManagerService groupManagerService,
                          final FacultyManagerService facultyManagerService) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.htmlService = htmlService;
        this.accessCourseService = accessCourseService;
        this.groupManagerService = groupManagerService;
        this.facultyManagerService = facultyManagerService;
    }

    /**
     * Retrieves a HTMl object from its id.
     *
     * @param id the id for the object
     * @return the html object
     */
    @GetMapping("/html/{id:\\d+}")
    @Secured(ROLE_USER)
    public HTML getHTML(@PathVariable("id") final String id) throws BadRequestException, NoAuthorizationException {
        if (isManager(id)) {
            if (htmlService.getHTML(id).isPresent()) {
                return htmlService.getHTML(id).get();
            } else {
                throw new BadRequestException();
            }
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }

    /**
     * update Html.
     *
     * @param id the id of the html
     * @param htmlCmd the HTMLCmd containg updated information
     * @return HTML
     */
    @PutMapping("/html/{id:\\d+}")
    @Secured(ROLE_USER)
    public HTML update(@PathVariable("id") final String id, @RequestBody final HTMLCmd htmlCmd)
            throws NoAuthorizationException {
        if (isManager(id)) {
            final HTML html = new HTML(htmlCmd.getHtmlString(), htmlCmd.getReview(), htmlCmd.getName());
            return htmlService.updateHTML(id, html);
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }

    }

    private boolean isManager(final String id) {
        return isManager(id, userService, htmlService, accessCourseService, employeeService, groupManagerService,
                facultyManagerService);
    }

    /**
     * Checks if the current user is a manager.
     *
     * @param id the id of the html
     * @param userService the userService
     * @param htmlService the htmlService
     * @param accessCourseService the accessCourseService
     * @param employeeService the employeeService
     * @param groupManagerService the groupManagerService
     * @param facultyManagerService the facultyManagerService
     * @return wether the current user is manager of the html object
     */
    public static boolean isManager(final String id, final UserService userService, final HTMLService htmlService,
                             final AccessCourseService accessCourseService, final EmployeeService employeeService,
                             final GroupManagerService groupManagerService,
                             final FacultyManagerService facultyManagerService) {
        final Optional<HTML> htmlOptional = htmlService.getHTML(id);
        if (htmlOptional.isEmpty()) {
            throw new BadRequestException();
        }
        final List<AccessCourse> courses = accessCourseService.getCourses();
        AccessCourse course = null;
        AccessGroup group = null;
        Faculty faculty = null;
        for (final AccessCourse accessCourse : courses) {
            if (accessCourse.getHtmlList().contains(htmlOptional.get())) {
                course = accessCourse;
                group = course.getGroup();
                faculty = group.getFaculty();
            }
        }
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) userService.loadUserByUsername(auth.getName());
        return isEmployee(user, course, employeeService) || isGroupManager(user, group, groupManagerService)
                || isFacultyManager(user, faculty, facultyManagerService);
    }

    @PostMapping("/html")
    @Secured(ROLE_USER)
    public HTML store(@RequestBody final HTMLCmd htmlCmd) {
        return htmlService.createHTML(htmlCmd.getHtmlString(), htmlCmd.getReview(), htmlCmd.getName());
    }
}
