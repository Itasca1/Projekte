package gpse.example.web;

import gpse.example.domain.cmds.AccessGroupCmd;
import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
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

import static gpse.example.web.ManagerHelper.isFacultyManager;
import static gpse.example.web.ManagerHelper.isGroupManager;

/**
 * Controller for groups.
 */
@SuppressWarnings({"ClassCanBeRecord", "PMD.ExcessiveImports"})
@RestController
@CrossOrigin
@RequestMapping("/api")
public class AccessGroupController {
    private static final String AUTHORIZATION_FAILED = "You are not authorized";
    private static final String ROLE_USER = "ROLE_USER";
    private final AccessGroupService accessGroupService;
    private final AccessCourseService courseService;
    private final GroupManagerService groupManagerService;
    private final FacultyManagerService facultyManagerService;
    private final UserService userService;
    private final AccessGroupService groupService;
    private final FacultyService facultyService;
    private final EmployeeService employeeService;


    /**
     * Constructor.
     *
     * @param accessGroupService    AccessGroupService
     * @param courseService         CourseService
     * @param facultyManagerService FacultyService
     * @param groupManagerService   GroupManagerService
     * @param userService           UserService
     * @param groupService          GroupService
     * @param facultyService        FacultyService
     * @param employeeService       EmployeeService
     */
    @Autowired
    @SuppressWarnings("ParameterNumber")
    public AccessGroupController(final AccessGroupService accessGroupService,
                                 final AccessCourseService courseService,
                                 final FacultyManagerService facultyManagerService,
                                 final GroupManagerService groupManagerService,
                                 final UserService userService,
                                 final AccessGroupService groupService,
                                 final FacultyService facultyService,
                                 final EmployeeService employeeService) {
        this.accessGroupService = accessGroupService;
        this.courseService = courseService;
        this.groupManagerService = groupManagerService;
        this.userService = userService;
        this.groupService = groupService;
        this.facultyManagerService = facultyManagerService;
        this.facultyService = facultyService;
        this.employeeService = employeeService;
    }

    /**
     * Loads all groups that are in the specified faculty.
     * This faculty is represeneted bs its id.
     *
     * @param facultyId id of faculty
     * @return all groups of the faculty
     */
    @GetMapping("/faculty-groups/{faculty-id:\\d+}")
    @Secured(ROLE_USER)
    public List<AccessGroup> getGroupsByFacultyId(@PathVariable("faculty-id") final String facultyId) {
        final List<AccessGroup> allGroups = new ArrayList<>();

        for (final AccessGroup group : accessGroupService.getAllGroups()) {
            if (Objects.equals(group.getFaculty().getId(), Long.valueOf(facultyId))) {
                allGroups.add(group);
            }
        }

        return allGroups;
    }

    /**
     * Returns Groups for a User via GET-Request.
     *
     * @return myGroups
     */
    @GetMapping("/groups")
    @Secured(ROLE_USER)
    public List<AccessGroup> getGroups() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final List<GroupManager> usergroups = groupManagerService.getGroupManagerByUser((User) author);
        final List<AccessGroup> myGroups = new ArrayList<>();
        for (final GroupManager manager : usergroups) {
            if (!myGroups.contains(manager.getAccessGroup())) {
                myGroups.add(manager.getAccessGroup());
            }
        }
        return myGroups;
    }

    @GetMapping("/allgroups")
    //@Secured(ROLE_USER)
    public List<AccessGroup> getAllGroups() {
        return accessGroupService.getAllGroups();
    }

    /**
     * showGroup method.
     *
     * @param id id of group
     * @return Group with id
     */
    @GetMapping("/groups/{id:\\d+}")
    @Secured(ROLE_USER)
    public AccessGroup getGroup(@PathVariable("id") final Long id) throws NoAuthorizationException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());

        if (accessGroupService.getGroupById(id).isPresent()) {
            final AccessGroup group = accessGroupService.getGroupById(id).get();
            final Faculty faculty = group.getFaculty();
            if (FacultyController.userInFaculty((User) author, faculty, facultyManagerService, groupManagerService,
                    employeeService, groupService)) {
                return accessGroupService.getGroupById(id).get();
            } else {
                throw new NoAuthorizationException(AUTHORIZATION_FAILED);
            }
        } else {
            throw new BadRequestException();
        }
    }

    /**
     * Creates new AccessGroups via a POST-Request.
     *
     * @param accessGroupCmd AccessGroupCmd
     * @return created AccessGroup
     */
    @PostMapping("/groups")
    @Secured(ROLE_USER)
    public AccessGroup store(@RequestBody final AccessGroupCmd accessGroupCmd)
            throws NoAuthorizationException, NotFoundException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final List<FacultyManager> userFaculties = facultyManagerService.getFacultyManagersByUser((User) author);
        final Optional<Faculty> possibleFaculty = facultyService.getFacultyById(accessGroupCmd.getFacultyID());
        if (possibleFaculty.isPresent()) {
            final Faculty storeFaculty = possibleFaculty.get();
            boolean isFacultyManager = false;
            // when users manage the faculty where we want to save the course
            for (final FacultyManager facultyManager : userFaculties) {
                if (Objects.equals(facultyManager.getFaculty().getId(), storeFaculty.getId())) {
                    isFacultyManager = true;
                    break;
                }
            }
            if (isFacultyManager) {
                final AccessGroup newGroup = accessGroupService.createAccessGroup(accessGroupCmd.getName(),
                        accessGroupCmd.getSemester(), storeFaculty);
                final GroupManager groupManager = new GroupManager((User) author, newGroup);
                groupManagerService.createGroupManager(groupManager);
                return newGroup;
            } else {
                throw new NoAuthorizationException(AUTHORIZATION_FAILED);
            }
        } else {
            throw new NotFoundException("Faculty not found.");
        }


    }

    /**
     * Updates a AccessGroup via PUT-Request.
     *
     * @param id             ID of AccessGroup
     * @param accessGroupCmd AccessGroupCmd
     * @return updated AccessGroup
     */
    @PutMapping("/groups/{id:\\d+}")
    @Secured(ROLE_USER)
    public AccessGroup update(@PathVariable("id") final String id, @RequestBody final AccessGroupCmd accessGroupCmd)
            throws NoAuthorizationException {
        if (manageAbove(Long.valueOf(id))) {
            final AccessGroup group = new AccessGroup(accessGroupCmd.getName(), accessGroupCmd.getSemester(),
                    accessGroupCmd.getFaculty());
            return accessGroupService.updateAccessGroup(id, group);
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }


    /**
     * Calculates the score of an AccessGroup object.
     *
     * @param group the group
     * @return score
     */
    public int getScoreByGroup(final AccessGroup group) {
        final List<AccessCourse> coursesOfGroup = courseService.getCoursesByGroup(group);
        int totalScore = 0;
        int groupLength = 0;
        for (final AccessCourse course : coursesOfGroup) {
            if (course.isWeighted()) {
                totalScore += 2 * course.getScore();
                groupLength += 2;
            } else {
                totalScore += course.getScore();
                groupLength++;
            }
        }
        if (coursesOfGroup.isEmpty()) {
            return 0;
        }
        return Math.round(((float) totalScore) / groupLength);
    }


    /**
     * Calculates the score for a group.
     *
     * @param id the id of the group
     * @return the score
     */
    @GetMapping("/group/{id:\\d+}/score")
    public int getScore(@PathVariable("id") final String id) throws NoAuthorizationException {
        final AccessGroup group = getGroup(Long.valueOf(id));
        return getScoreByGroup(group);
    }

    /**
     * Calculates the average score over all groups in the data base.
     *
     * @return average score
     */
    @GetMapping("/avgAllGroups")
    public int getAvgAllGroups() {
        final List<AccessGroup> allGroups = accessGroupService.getAllGroups();
        int sum = 0;
        int avgAllGroups;
        for (final AccessGroup currentGroup : allGroups) {
            sum += getScoreByGroup(currentGroup);
        }
        avgAllGroups = Math.round(((float) sum) / allGroups.size());
        return avgAllGroups;
    }


    /**
     * Deletes an AccessGroup via PUT-Request.
     *
     * @param id             ID of AccessGroup
     * @return updated AccessGroup
     */
    @PutMapping("/group/{id:\\d+}")
    @Secured(ROLE_USER)
    public AccessGroup delete(@PathVariable("id") final String id)
            throws NoAuthorizationException {
        if (manageAbove(Long.valueOf(id))) {
            AccessGroup group;
            if (accessGroupService.getGroupById(Long.valueOf(id)).isPresent()) {
                group = accessGroupService.getGroupById(Long.valueOf(id)).get();
                return accessGroupService.deleteGroup(group);
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
     * @param id the id of the group.
     * @return wether the current user is manager of the group
     */
    public boolean manageAbove(final Long id) {
        if (accessGroupService.getGroupById(id).isEmpty()) {
            throw new BadRequestException();
        }
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) userService.loadUserByUsername(auth.getName());
        final AccessGroup group = accessGroupService.getGroupById(id).get();
        final Faculty faculty = group.getFaculty();
        return isGroupManager(user, group, groupManagerService)
                || isFacultyManager(user, faculty, facultyManagerService);
    }


    /**
     * Returns true if user is allowed to edit an AccessGroup.
     *
     * @param id ID of AccessGroup.
     * @return true if user manages group or manages faculty of group, otherwiese false.
     */
    @GetMapping("/group-edit/{id:\\d+}")
    @Secured(ROLE_USER)
    public boolean allowedToEdit(@PathVariable("id") final String id) {
        return manageAbove(Long.valueOf(id));
    }
}

