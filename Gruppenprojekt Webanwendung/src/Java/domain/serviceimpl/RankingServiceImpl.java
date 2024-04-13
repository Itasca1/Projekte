package gpse.example.domain.serviceimpl;

import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Employee;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.FacultyManager;
import gpse.example.domain.entities.GroupManager;
import gpse.example.domain.entities.Rank;
import gpse.example.domain.entities.User;
import gpse.example.domain.services.AccessCourseService;
import gpse.example.domain.services.AccessGroupService;
import gpse.example.domain.services.EmployeeService;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.domain.services.FacultyService;
import gpse.example.domain.services.GroupManagerService;
import gpse.example.domain.services.RankingService;
import gpse.example.domain.services.UserService;
import gpse.example.web.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Implementation of {@link RankingService} needed to load review objects.
 */
@Service
public class RankingServiceImpl implements RankingService {
    private final UserService userService;
    private final AccessCourseService courseService;
    private final AccessGroupService groupService;
    private final FacultyService facultyService;
    private final FacultyManagerService facultyManagerService;
    private final GroupManagerService groupManagerService;
    private final EmployeeService employeeService;

    /**
     * Constructor for RankingServiceImpl with all necessary services.
     *
     * @param userService the UserService
     * @param accessCourseService the AccessCourseService
     * @param accessGroupService the AccessGroupService
     * @param facultyService the FacultyService
     * @param facultyManagerService the FacultyManagerService
     * @param groupManagerService the GroupManagerService
     * @param employeeService the EmployeeService
     */
    @Autowired
    public RankingServiceImpl(final UserService userService, final AccessCourseService accessCourseService,
                              final AccessGroupService accessGroupService, final FacultyService facultyService,
                              final FacultyManagerService facultyManagerService,
                              final GroupManagerService groupManagerService, final EmployeeService employeeService) {
        this.userService = userService;
        this.courseService = accessCourseService;
        this.groupService = accessGroupService;
        this.facultyService = facultyService;
        this.facultyManagerService = facultyManagerService;
        this.groupManagerService = groupManagerService;
        this.employeeService = employeeService;
    }

    @Override
    public List<Rank> getFacultyRanking(final String userID) {
        final Optional<User> userOptional = userService.getUser(userID);
        if (userOptional.isEmpty()) {
            throw new BadRequestException();
        }
        final User user = userOptional.get();
        final List<Faculty> faculties = facultyService.getAllFaculties();
        faculties.sort((facA, facB) -> calculateFacultyScore(facB) - calculateFacultyScore(facA));
        int rank = 1;
        final ArrayList<Rank> ranked = new ArrayList<>();
        for (final Faculty faculty : faculties) {
            ranked.add(new Rank(rank, faculty.getName(), calculateFacultyScore(faculty),
                    checkUserInFaculty(faculty, user)));
            rank++;
        }
        return ranked;
    }

    @Override
    public List<Rank> getGroupRanking(final String userID) {
        final List<Rank> ranked = new ArrayList<>();
        final List<AccessGroup> groups = groupService.getAllGroups();
        final Optional<User> userPromise = userService.getUser(userID);
        try {
            if (userPromise.isEmpty()) {
                throw new BadRequestException();
            }
            final User user = userPromise.get();
            groups.sort((groupA, groupB) -> calculateGroupScore(groupB) - calculateGroupScore(groupA));
            int rank = 1;
            for (final AccessGroup group : groups) {
                ranked.add(new Rank(rank, group.getName(), calculateGroupScore(group), checkUserInGroup(group, user)));
                rank++;
            }
        } catch (NoSuchElementException e) {
           return ranked;
        }
        return ranked;
    }

    @Override
    public List<Rank> getCourseRanking(final String userID) {
        final ArrayList<Rank> ranked = new ArrayList<>();
        final List<AccessCourse> courses = courseService.getCourses();
        final Optional<User> userPromise = userService.getUser(userID);
        try {
            if (userPromise.isEmpty()) {
                throw new BadRequestException();
            }
            final User user = userPromise.get();
            courses.sort((courseA, courseB) -> courseB.getScore() - courseA.getScore());
            int rank = 1;
            for (final AccessCourse cou : courses) {
                if (cou.getVisible() || checkUserInCourse(cou, user)) {
                    ranked.add(new Rank(rank, cou.getName(), cou.getScore(), checkUserInCourse(cou, user)));
                }
                rank++;

            }
        } catch (NoSuchElementException e) {
            return ranked;
        }
        return ranked;
    }

    @Override
    public List<Rank> getUserRanking(final String userID) {
        final ArrayList<Rank> ranked = new ArrayList<>();
        final List<User> users = userService.getUsers();
        try {
            users.sort((userA, userB) -> calculateUserScore(userB) - calculateUserScore(userA));
            int rank = 1;
            for (final User user : users) {
                if (user.getVisible() || user.getId().toString().compareTo(userID) == 0) {
                    ranked.add(new Rank(rank, user.getFirstname() + " " + user.getLastname(),
                        calculateUserScore(user), user.getId().toString().compareTo(userID) == 0));
                }
                rank++;

            }
        } catch (NoSuchElementException e) {
            return ranked;
        }
        return ranked;
    }

    private int calculateFacultyScore(final Faculty faculty) {
        int scoreSum = 0;
        final List<AccessGroup> groups = groupService.getGroupsByFaculty(faculty);
        if (groups.isEmpty()) {
            return 0;
        } else {
            for (final AccessGroup group : groups) {
                scoreSum += calculateGroupScore(group);
            }
            if (groups.isEmpty()) {
                return 0;
            } else {
                return scoreSum / groups.size();
            }
        }
    }

    private int calculateGroupScore(final AccessGroup group) {
        int scoreSum = 0;
        final List<AccessCourse> courses = courseService.getCoursesByGroup(group);
        if (courses.isEmpty()) {
            return 0;
        } else {
            for (final AccessCourse course : courses) {
                scoreSum += course.getScore();
            }
            return scoreSum / courses.size();
        }
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private int calculateUserScore(final User user) {
        int noEnitities = 3;
        int facultyScore = 0;
        int groupScore = 0;
        int courseScore = 0;

        final List<FacultyManager> facultyManagers = facultyManagerService.getFacultyManagersByUser(user);
        if (facultyManagers.isEmpty()) {
            noEnitities--;
        } else {
            for (final FacultyManager fm : facultyManagers) {
                facultyScore += calculateFacultyScore(fm.getFaculty());
            }
            facultyScore /= facultyManagers.size();
        }

        final List<GroupManager> groupManagers = groupManagerService.getGroupManagerByUser(user);
        if (groupManagers.isEmpty()) {
            noEnitities--;
        } else {
            for (final GroupManager gm : groupManagers) {
                groupScore += calculateGroupScore(gm.getAccessGroup());
            }
            groupScore /= groupManagers.size();
        }

        final List<Employee> employeeList = employeeService.getEmployeesByUser(user);
        if (employeeList.isEmpty()) {
            noEnitities--;
        } else {
            for (final Employee em : employeeList) {
                courseScore += em.getAccessCourse().getScore();
            }
            if (employeeList.isEmpty()) {
                courseScore = 0;
            } else {
                courseScore /= employeeList.size();
            }
        }

        if (noEnitities > 0) {
            return (facultyScore + groupScore + courseScore) / noEnitities;
        } else {
            return 0;
        }
    }

    private boolean checkUserInFaculty(final Faculty faculty, final User user) {
        if (facultyManagerService.searchForFacultyManager(user, faculty) != null) {
            return true;
        }

        final List<AccessGroup> groups = groupService.getGroupsByFaculty(faculty);
        for (final AccessGroup group : groups) {
            if (checkUserInGroup(group, user)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkUserInGroup(final AccessGroup group, final User user) {
        if (groupManagerService.searchForGroupManager(user, group) != null)  {
            return true;
        }
        final List<AccessCourse> courses = courseService.getCoursesByGroup(group);
        for (final AccessCourse course : courses) {
            if (checkUserInCourse(course, user)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkUserInCourse(final AccessCourse course, final User user) {
        return employeeService.searchForEmployee(user, course) != null;
    }
}
