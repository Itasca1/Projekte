package gpse.example.web;

import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.FacultyManager;
import gpse.example.domain.cmds.FacultyManagerCmd;
import gpse.example.domain.entities.User;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.domain.services.FacultyService;
import gpse.example.domain.services.NoAuthorizationException;
import gpse.example.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller for FacultyManager.
 */
@RestController
@CrossOrigin
@RequestMapping("/api")
public class FacultyManagerController {
    private static final String AUTHORIZATION_FAILED = "You are not authorized";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private final FacultyManagerService facultyManagerService;
    private final UserService userservice;
    private final FacultyService facultyService;

    /**
     * Constructor.
     *
     * @param facultyManagerService facultyManager-service
     * @param userService           userService
     * @param facultyService        facultyService
     */
    @Autowired
    public FacultyManagerController(final FacultyManagerService facultyManagerService, final UserService userService,
                                    final FacultyService facultyService) {
        this.facultyManagerService = facultyManagerService;
        this.userservice = userService;
        this.facultyService = facultyService;
    }

    /**
     * Returns all Managers (as list of User objects) of a given Faculty.
     *
     * @param facultyId ID of given Faculty
     * @return managers from given Faculty
     */
    @GetMapping("/faculty-managers/{faculty-id:\\d+}")
    //@Secured(ROLE_USER)
    public List<User> getManagerFromFaculty(@PathVariable("faculty-id") final String facultyId) {
        Faculty faculty;
        if (facultyService.getFacultyById(Long.valueOf(facultyId)).isPresent()) {
            faculty = facultyService.getFacultyById(Long.valueOf(facultyId)).get();
        } else {
            return new ArrayList<>();
        }
        final List<User> users = new ArrayList<>();
        final List<FacultyManager> managers = facultyManagerService.getFacultyManagerByFaculty(faculty);
        managers.forEach(manager -> users.add(manager.getUser()));


        return users;
    }

    @GetMapping("/facultyManager")
    //@Secured(ROLE_USER)
    public List<FacultyManager> showFacultyManagers() {
        return facultyManagerService.getFacultyManagers();
    }

    /**
     * get FacultyManager.
     *
     * @param id pathvariable of FacultyManager
     * @return FacultyManager with id
     */
    @GetMapping("/facultyManager/{id:\\d+}")
    @Secured(ROLE_USER)
    public FacultyManager showFacultyManager(@PathVariable("id") final String id) {
        if (facultyManagerService.getFacultyManager(id).isPresent()) {
            return facultyManagerService.getFacultyManager(id).get();
        } else {
            throw new BadRequestException();
        }
    }

    /**
     * Returns all faculty manager roles of the current user.
     *
     * @return All faculty managers of current user
     */
    @GetMapping("/getMyFacManagers")
    @Secured(ROLE_USER)
    public List<FacultyManager> getMyFacManagers() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails author = userservice.loadUserByUsername(auth.getName());
        return facultyManagerService.getFacultyManagersByUser((User) author);
    }

    /**
     * Get all faculty managers that the current user is allowed to delete.
     *
     * @return List of faculty managers that can be deleted by current user
     */
    @GetMapping("getMyDeletableFacManagers")
    @Secured(ROLE_USER)
    public List<FacultyManager> getMyDeletableFacManagers() {
        final List<FacultyManager> myFacManagers = getMyFacManagers();
        final List<FacultyManager> allFacManagers = allFacultyManagers();
        final List<FacultyManager> myDeletableFacManagers = new ArrayList<>();
        for (final FacultyManager facManager : allFacManagers) {
            for (final FacultyManager myFacManager : myFacManagers) {
                if (facManager.getFaculty().getId().equals(myFacManager.getFaculty().getId())) {
                    myDeletableFacManagers.add(facManager);
                }
            }
        }
        return myDeletableFacManagers;
    }


    /**
     * Returns all faculty managers in DB as list of FacultyManager objects.
     *
     * @return All faculty managers in Db
     */
    @GetMapping("/getAllFacultyManagers")
    @Secured(ROLE_USER)
    public List<FacultyManager> allFacultyManagers() {
        return facultyManagerService.getFacultyManagers();
    }


    /**
     * Add FacultyManager.
     *
     * @param facultyManagerCmd Commandobject
     * @return create facultyManager
     */
    @PostMapping("/AddFacultyManager")
    @Secured({ROLE_ADMIN, ROLE_USER})
    public FacultyManager store(@RequestBody final FacultyManagerCmd facultyManagerCmd)
            throws BadRequestException, NoAuthorizationException {
        final User user = userservice.loadUserById(facultyManagerCmd.getUserID());
        final Faculty faculty = facultyService.getFacultyById(facultyManagerCmd.getFacultyID())
                .orElseThrow(BadRequestException::new);
        FacultyManager oldFacultyManager;
        try {
            oldFacultyManager = facultyManagerService.searchForFacultyManager(user, faculty);
        } catch (UsernameNotFoundException exception) {
            return facultyManagerService.saveFacultyManager(new FacultyManager(user, faculty));
        }
        if (Objects.nonNull(oldFacultyManager)) {
            throw new BadRequestException();
        }
        final FacultyManager facultyManager = new FacultyManager(user, faculty);
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User author = (User) userservice.loadUserByUsername(auth.getName());
        if (author.getRoles().contains(ROLE_ADMIN)) {
            return facultyManagerService.saveFacultyManager(facultyManager);
        }

        // test whether the user Manages the Faculty
        boolean isManager = false;
        final List<FacultyManager> userfaculties = facultyManagerService.getFacultyManagersByUser(author);
        for (final FacultyManager fac : userfaculties) {
            if (fac.getFaculty().getId().equals(faculty.getId())) {
                isManager = true;
                break;
            }
        }
        if (isManager) {
            return facultyManagerService.saveFacultyManager(facultyManager);
        }
        throw new NoAuthorizationException(AUTHORIZATION_FAILED);
    }

    /**
     * Delete FacultyManager.
     *
     * @param facultyManagerCmd FacultyManager
     * @throws BadRequestException FacultyManager not found.
     */
    @PostMapping("/deleteFacultyManager")
    @Secured({ROLE_ADMIN, ROLE_USER})
    public void deleteFacultyManager(@RequestBody final FacultyManagerCmd facultyManagerCmd)
            throws BadRequestException, NoAuthorizationException {
        final User user = userservice.loadUserById(facultyManagerCmd.getUserID());
        final Faculty faculty = facultyService.getFacultyById(facultyManagerCmd.getFacultyID())
                .orElseThrow(BadRequestException::new);
        FacultyManager oldFacultyManager;
        oldFacultyManager = facultyManagerService.searchForFacultyManager(user, faculty);
        if (Objects.isNull(oldFacultyManager)) {
            throw new BadRequestException();
        }
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final User author = (User) userservice.loadUserByUsername(auth.getName());
        if (author.getRoles().contains(ROLE_ADMIN)) {
            facultyManagerService.deleteFacultyManager(oldFacultyManager);
        } else {
            // test whether the user Manages the Faculty
            final List<FacultyManager> userfaculties = facultyManagerService.getFacultyManagersByUser(author);
            for (final FacultyManager fac : userfaculties) {
                if (fac.getFaculty().getId().equals(faculty.getId())) {
                    facultyManagerService.deleteFacultyManager(oldFacultyManager);
                } else {
                    throw new NoAuthorizationException(AUTHORIZATION_FAILED);
                }
            }
        }

    }
}

