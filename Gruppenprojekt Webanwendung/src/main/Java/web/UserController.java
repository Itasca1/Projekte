package gpse.example.web;

import gpse.example.domain.cmds.ConfirmationTokenCmd;
import gpse.example.domain.cmds.UserCmd;
import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.ConfirmationTokens;
import gpse.example.domain.entities.FacultyManager;
import gpse.example.domain.entities.GroupManager;
import gpse.example.domain.entities.User;
import gpse.example.domain.services.AccessGroupService;
import gpse.example.domain.services.ConfirmationTokenService;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.domain.services.GroupManagerService;
import gpse.example.domain.services.NoAuthorizationException;
import gpse.example.domain.services.NotFoundException;
import gpse.example.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for User.
 */
@SuppressWarnings("ClassCanBeRecord")
@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {
    private static final String AUTHORIZATION_FAILED = "You are not authorized";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final UserService userService;
    private final GroupManagerService groupManagerService;
    private final FacultyManagerService facultyManagerService;
    private final AccessGroupService groupService;
    private final ConfirmationTokenService confirmationTokenService;

    /**
     * Constructor.
     *
     * @param userService              for accessing functions for users
     * @param groupManagerService      for accessing functions for groupmanagers
     * @param groupService             for accessing functions for groups
     * @param facultyManagerService    for accessing functions for facultymangers
     * @param confirmationTokenService for accessing functions for confirmationTokens
     */
    @Autowired
    public UserController(final UserService userService, final GroupManagerService groupManagerService,
                          final AccessGroupService groupService, final FacultyManagerService facultyManagerService,
                          final ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.groupManagerService = groupManagerService;
        this.facultyManagerService = facultyManagerService;
        this.groupService = groupService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @GetMapping("/user")
    public List<User> showUsers() {
        return userService.getUsers();
    }

    /**
     * get User.
     *
     * @param id pathvariable of User
     * @return User with id
     */
    @GetMapping("/user/{id:\\d+}")
    public User showUser(@PathVariable("id") final String id) {
        if (userService.getUser(id).isPresent()) {
            return userService.getUser(id).get();
        } else {
            throw new BadRequestException();
        }
    }

    /**
     * get User with his username.
     *
     * @param username pathvariable of User
     * @return User with Username
     */
    @GetMapping("/username/{username:\\w+}")
    @Secured(ROLE_USER)
    public User showUserWithUsername(@PathVariable("username") final String username) {
        return userService.loadUserByMail(username);
    }

    /**
     * store User.
     *
     * @param userCmd Commandobject
     * @return create User
     */
    @PostMapping("/users")
    public User store(@RequestBody final UserCmd userCmd) throws NotFoundException {
        User user = new User(userCmd.getEmail(),
                userCmd.getFirstname(), userCmd.getLastname(), userCmd.getRecentlyVisitedCourses(),
                userCmd.getPassword(), userCmd.getVisible(), userCmd.isMenuOpen());
        user = userService.createUser(user, ROLE_USER);
        /*final AccessGroup group = groupService.getGroupByName("Algorithmen & Datenstrukturen");
        final GroupManager groupManager = new GroupManager(user, group);
        final FacultyManager facultyManager = new FacultyManager(user, group.getFaculty());
        groupManagerService.saveGroupManager(groupManager);
        facultyManagerService.saveFacultyManager(facultyManager);*/
        return user;
    }

    /**
     * update UserInfo.
     *
     * @param userCmd Commandobject
     * @return updated User
     */
    @PutMapping("/userInfo")
    @Secured(ROLE_USER)
    public User updateInfo(@RequestBody final UserCmd userCmd) {
        final User user = new User(userCmd.getEmail(), userCmd.getFirstname(), userCmd.getLastname(),
                userCmd.getRecentlyVisitedCourses(), userCmd.getPassword(), userCmd.getVisible(), userCmd.isMenuOpen());
        user.setUserScoreDisabled(userCmd.getVisible());
        final User usi = userService.updateUserInfo(user);
        if (usi == null) {
            throw new BadRequestException();
        }
        return usi;
    }

    /**
     * update Userinformation.
     *
     * @param id the id of the user
     * @param userCmd the UserCmd object containing updated information
     * @return the updated user
     */
    @PutMapping("/userInformation/{id:\\d+}")
    public User updateInformation(@PathVariable("id") final String id, @RequestBody final UserCmd userCmd)
            throws NoAuthorizationException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User author = (User) userService.loadUserByUsername(auth.getName());
        if (author.getId().equals(Long.valueOf(id))) {
            final User user =
                    new User(userCmd.getEmail(), userCmd.getFirstname(),
                            userCmd.getLastname(), new ArrayList<>(), "", userCmd.getVisible(),
                        userCmd.isMenuOpen());
            user.setId(Long.parseLong(id));
            user.setUserScoreDisabled(userCmd.getVisible());
            return userService.updateUserInformaton(user);
        } else {
            throw new NoAuthorizationException(AUTHORIZATION_FAILED);
        }
    }


    /**
     * add Role Admin to User with id.
     *
     * @param userCmd UserCmd
     * @return user
     */
    @PostMapping("/addAdmin")
    @Secured(ROLE_ADMIN)
    public User addAdmin(@RequestBody final UserCmd userCmd) {
        final User user = userService.loadUserById(userCmd.getId());
        if (user.getRoles().contains(ROLE_ADMIN)) {
            return user;
        }
        user.addRole(ROLE_ADMIN);
        return userService.saveUser(user);
    }

    @GetMapping("/user/{id:\\d+}/recentlyVisited")
    @Secured(ROLE_USER)
    public List<AccessCourse> getRecentlyVisitedCourses(@PathVariable final String id) {
        final User user = showUser(id);
        return user.getRecentlyVisitedCourses();
    }

    @GetMapping("/user/{id:\\d+}/hasMenuOpen")
    @Secured(ROLE_USER)
    public boolean hasMenuOpen(@PathVariable final String id) {
        final User user = showUser(id);
        return user.isMenuOpen();
    }

    /**
     * Verify User by setting AccountNonLocked to true with userEnabled.
     *
     * @param confirmationTokenCmd to get the information of the token
     * @throws NotFoundException if the confirmationToken doesn't exist
     */
    @PutMapping("/confirm_account")
    public void confirmUserAccount(@RequestBody final ConfirmationTokenCmd confirmationTokenCmd)
            throws NotFoundException {
        final String getToken = confirmationTokenCmd.getConfirmationToken();
        final ConfirmationTokens token = confirmationTokenService.findToken(getToken);
        if (token == null) {
            throw new NotFoundException("ConfirmationToken with " + getToken + " not found!");
        } else {
            final User user = userService.loadUserById(token.getUser().getId());
            user.setUserEnabled(true);
            userService.saveUser(user);
        }
    }

    @GetMapping("/token")
    public List<ConfirmationTokens> showToken() {
        return confirmationTokenService.getTokens();
    }

    /**
     * Returns userID.
     *
     * @return userID
     */
    @GetMapping("/myid")
    public Long myId() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final String name = author.getUsername();
        final User user = (User) userService.loadUserByUsername(name);
        return user.getId();
    }

    /**
     * Checks for ROLE_ADMIN.
     *
     * @return Boolean for Admin Role.
     */
    @GetMapping("/isadmin")
    public boolean isAdmin() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final String name = author.getUsername();
        final User user = (User) userService.loadUserByUsername(name);
        return user.getRoles().contains(ROLE_ADMIN);
    }

    /**
     * isGroupManager.
     *
     * @return roles
     */
    @GetMapping("/isgroupmanagerfor")
    public List<String> getRoles() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails currentUserDetails = userService.loadUserByUsername(auth.getName());
        final String userName = currentUserDetails.getUsername();
        final User user = (User) userService.loadUserByUsername(userName);
        return user.getRoles();
    }


    @PostMapping("/deleteUser")
    @Secured(ROLE_ADMIN)
    public void deleteUser(@RequestBody final UserCmd userCmd) {
        final User user = userService.loadUserById(userCmd.getId());
        userService.deleteUser(user);
    }

    /**
     * Checks for FacultyManager or Admin .
     *
     * @return Boolean for FacultyManager Role.
     */

    @GetMapping("/isFacultyManager")
    public boolean isFacultyManager() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final String name = author.getUsername();
        final User user = (User) userService.loadUserByUsername(name);
        final List<FacultyManager> facultyManagers = facultyManagerService.getFacultyManagersByUser(user);
        return !facultyManagers.isEmpty() || isAdmin();
    }

    /**
     * Checks for GroupManager or Admin.
     *
     * @return Boolean for GroupManager Role.
     */
    @GetMapping("/isGroupManager")
    public boolean isGroupManager() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userService.loadUserByUsername(auth.getName());
        final String name = author.getUsername();
        final User user = (User) userService.loadUserByUsername(name);
        final List<GroupManager> groupManagers = groupManagerService.getGroupManagerByUser(user);
        return !groupManagers.isEmpty() || isAdmin();
    }
}
