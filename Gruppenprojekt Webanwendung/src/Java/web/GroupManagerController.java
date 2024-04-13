package gpse.example.web;

import gpse.example.domain.cmds.GroupManagerCmd;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.GroupManager;
import gpse.example.domain.entities.User;
import gpse.example.domain.services.AccessGroupService;
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

import static gpse.example.web.ManagerHelper.isFacultyManager;
import static gpse.example.web.ManagerHelper.isGroupManager;

/**
 * Controller for GroupManager.
 */
@SuppressWarnings("ClassCanBeRecord")
@RestController
@CrossOrigin
@RequestMapping("/api")
public class GroupManagerController {
    private static final String AUTHORIZATION_FAILED = "You are not authorized";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private final GroupManagerService groupManagerService;
    private final AccessGroupService accessGroupService;
    private final UserService userService;
    private final FacultyManagerService facultyManagerService;

    /**
     * Constructor.
     *
     * @param groupManagerService   the groupManagerService
     * @param accessGroupService    the groupService
     * @param userService           the userService
     * @param facultyManagerService the facultyManagerService
     */
    @Autowired
    public GroupManagerController(final GroupManagerService groupManagerService,
                                  final AccessGroupService accessGroupService, final UserService userService,
                                  final FacultyManagerService facultyManagerService) {
        this.groupManagerService = groupManagerService;
        this.accessGroupService = accessGroupService;
        this.userService = userService;
        this.facultyManagerService = facultyManagerService;
    }

    /**
     * Returns all Managers (as list of User objects) of a given AccessGroup.
     *
     * @param groupID ID of given AccessGroup
     * @return managers from given AccessGroup
     */
    @GetMapping("/group-manager/{group-id:\\d+}")
    @Secured(ROLE_USER)
    public List<User> getManagerFromGroup(@PathVariable("group-id") final String groupID) {
        AccessGroup group;
        if (accessGroupService.getGroupById(Long.valueOf(groupID)).isPresent()) {
            group = accessGroupService.getGroupById(Long.valueOf(groupID)).get();
        } else {
            return new ArrayList<>();
        }
        final List<User> users = new ArrayList<>();
        final List<GroupManager> managers = groupManagerService.getGroupManagerByGroup(group);
        managers.forEach(manager -> users.add(manager.getUser()));
        return users;
    }

    /**
     * Returns groupManagers from user, via GET-Request.
     *
     * @return groupManagers
     */
    @GetMapping("/groupManager")
    //@Secured(ROLE_USER)
    public List<GroupManager> showGroupManagers() {
        return groupManagerService.getGroupManagers();
    }

    /**
     * get GroupManager.
     *
     * @param id pathvariable of GroupManager
     * @return GroupManager with id
     */
    @GetMapping("/groupManager/{id:\\d+}")
    @Secured(ROLE_USER)
    public GroupManager showGroupManager(@PathVariable("id") final String id) {
        if (groupManagerService.getGroupManager(id).isPresent()) {
            return groupManagerService.getGroupManager(id).get();
        } else {
            throw new BadRequestException();
        }
    }

    /**
     * Returns a List of group manager objects of the current user.
     *
     * @return List of group managers of current user
     */
    @GetMapping("getMyGroupManagers")
    @Secured(ROLE_USER)
    public List<GroupManager> getMyGroupManagers() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        return groupManagerService.getGroupManagerByUser((User) author);
    }

    /**
     * Returns a List of group manager objects that can be deleted by the current user.
     *
     * @return List of group manager that can be deleted by current user
     */
    @GetMapping("getMyDeletableGroupManagers")
    @Secured(ROLE_USER)
    public List<GroupManager> getMyDeletableGroupManagers() {
        final List<GroupManager> myGroupManagers = getMyGroupManagers();
        final List<GroupManager> allGroupManagers = showGroupManagers();
        final List<GroupManager> myDeletableGroupManagers = new ArrayList<>();
        for (final GroupManager groupManager : allGroupManagers) {
            for (final GroupManager myGroupManager : myGroupManagers) {
                if (groupManager.getAccessGroup().getId().equals(myGroupManager.getAccessGroup().getId())) {
                    myDeletableGroupManagers.add(groupManager);
                }
            }
        }
        return myDeletableGroupManagers;
    }

    /**
     * store GroupManager.
     *
     * @param groupManagerCmd Commandobject
     * @return create groupManager
     */
    @PostMapping("/groupManager")
    @Secured(ROLE_USER)
    public GroupManager store(@RequestBody final GroupManagerCmd groupManagerCmd) throws NoAuthorizationException {
        if (isManager(groupManagerCmd.getGroupID())) {
            final Optional<AccessGroup> groupOptional = accessGroupService.getGroupById(groupManagerCmd.getGroupID());
            if (groupOptional.isEmpty()) {
                throw new BadRequestException();
            }
            final AccessGroup accessGroup = groupOptional.get();
            final User user = userService.loadUserById(groupManagerCmd.getUserID());
            final GroupManager groupManager = new GroupManager(user, accessGroup);
            return groupManagerService.createGroupManager(groupManager);
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }

    /**
     * delete GroupManager.
     *
     * @param groupManagerCmd GroupManager
     * @throws BadRequestException GroupManager not found
     */
    @PostMapping("/deleteGroupManager")
    @Secured({ROLE_ADMIN, ROLE_USER})
    public void deleteGroupManager(@RequestBody final GroupManagerCmd groupManagerCmd)
            throws BadRequestException, NoAuthorizationException {
        final User user = userService.loadUserById(groupManagerCmd.getUserID());
        final AccessGroup group = accessGroupService.getGroupById(groupManagerCmd.getGroupID())
                .orElseThrow(BadRequestException::new);
        GroupManager oldGroupManager;
        oldGroupManager = groupManagerService.searchForGroupManager(user, group);
        if (Objects.isNull(oldGroupManager)) {
            throw new BadRequestException();
        }
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User author = (User) userService.loadUserByUsername(auth.getName());
        if (author.getRoles().contains(ROLE_ADMIN)) {
            groupManagerService.deleteGroupManager(oldGroupManager);
        } else {
            // test whether the user Manages the Group
            final List<GroupManager> userGroupManagers = groupManagerService.getGroupManagerByUser(author);
            for (final GroupManager currentGroupManager : userGroupManagers) {
                if (currentGroupManager.getAccessGroup().getId().equals(group.getId())) {
                    groupManagerService.deleteGroupManager(oldGroupManager);
                } else {
                    throw new NoAuthorizationException(AUTHORIZATION_FAILED);
                }
            }
        }
    }

    /**
     * isManager.
     *
     * @param id the id of the group
     * @return wether the current user is manager for the group
     */
    public boolean isManager(final Long id) {
        if (accessGroupService.getGroupById(id).isEmpty()) {
            throw new BadRequestException();
        }
        final AccessGroup group = accessGroupService.getGroupById(id).get();
        final Faculty faculty = group.getFaculty();
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) userService.loadUserByUsername(auth.getName());
        return isGroupManager(user, group, groupManagerService)
                || isFacultyManager(user, faculty, facultyManagerService);
    }
}

