package gpse.example.web;

import gpse.example.domain.cmds.ReviewCmd;
import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.HTML;
import gpse.example.domain.entities.Review;
import gpse.example.domain.entities.User;
import gpse.example.domain.services.AccessCourseService;
import gpse.example.domain.services.EmployeeService;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.domain.services.GroupManagerService;
import gpse.example.domain.services.NoAuthorizationException;
import gpse.example.domain.services.ReviewService;
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
 * Controller for review objects.
 */
@SuppressWarnings({"ClassCanBeRecord", "PMD.ExcessiveImports"})
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ReviewController {
    private static final String AUTHORIZATION_FAILED = "You are not authorized";
    private static final String ROLE_USER = "ROLE_USER";
    private final ReviewService reviewService;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final AccessCourseService accessCourseService;
    private final GroupManagerService groupManagerService;
    private final FacultyManagerService facultyManagerService;


    /**
     * Constructor.
     *
     * @param reviewService         the reviewService
     * @param userService           the userService
     * @param employeeService       the employeeService
     * @param accessCourseService   the courseService
     * @param groupManagerService   the groupManagerService
     * @param facultyManagerService the facultyManagerService
     */
    @Autowired
    public ReviewController(final ReviewService reviewService, final UserService userService,
                            final EmployeeService employeeService, final AccessCourseService accessCourseService,
                            final GroupManagerService groupManagerService,
                            final FacultyManagerService facultyManagerService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.employeeService = employeeService;
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
    @GetMapping("/review/{id:\\d+}")
    @Secured(ROLE_USER)
    public Review getReview(@PathVariable("id") final String id) {
        final Optional<Review> reviewOptional = reviewService.getReview(id);
        if (reviewOptional.isPresent()) {
            return reviewOptional.get();
        }
        throw new BadRequestException();
    }

    /**
     * update Review.
     *
     * @param id        the id of the review
     * @param reviewCmd the ReviewCMD containing updated information
     * @return review
     */
    @PutMapping("/review/{id:\\d+}")
    @Secured(ROLE_USER)
    public Review update(@PathVariable("id") final String id, @RequestBody final ReviewCmd reviewCmd)
            throws NoAuthorizationException {
        if (isManager(id)) {
            return reviewService.updateReview(id, reviewCmd);
        }
        throw new NoAuthorizationException(AUTHORIZATION_FAILED);
    }

    /**
     * Stores a Review in the database.
     *
     * @param reviewCmd object that capsules all information needed to store the review.
     * @return the stores Review object.
     */
    @PostMapping("/review")
    @Secured(ROLE_USER)
    public Review store(@RequestBody final ReviewCmd reviewCmd) {
        return reviewService.createReview(reviewCmd.getReviewComplete(), reviewCmd.getReviewPosition(),
                reviewCmd.getListReviews(), reviewCmd.getFormattingReviews(),
                reviewCmd.getLinkReviews(), reviewCmd.getImageReviews(), reviewCmd.getVideoReviews(),
                reviewCmd.getAutomaticReviews());
    }

    /**
     * isManager function.
     *
     * @param id the id of the review
     * @return wether the current user is manager for the review
     */
    public boolean isManager(final String id) {
        final Optional<Review> reviewOptional = reviewService.getReview(id);
        if (reviewOptional.isEmpty()) {
            throw new BadRequestException();
        }
        final List<AccessCourse> courses = accessCourseService.getCourses();
        AccessCourse course = null;
        AccessGroup group = null;
        Faculty faculty = null;
        for (final AccessCourse accessCourse : courses) {
            for (final HTML html : accessCourse.getHtmlList()) {
                if (html.getReview().equals(reviewOptional.get())) {
                    course = accessCourse;
                    group = course.getGroup();
                    faculty = group.getFaculty();
                }
            }
        }
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) userService.loadUserByUsername(auth.getName());
        return isEmployee(user, course, employeeService) || isGroupManager(user, group, groupManagerService)
                || isFacultyManager(user, faculty, facultyManagerService);
    }
}
