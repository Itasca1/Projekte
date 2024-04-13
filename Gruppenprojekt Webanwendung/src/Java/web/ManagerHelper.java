package gpse.example.web;

import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Employee;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.FacultyManager;
import gpse.example.domain.entities.GroupManager;
import gpse.example.domain.entities.User;
import gpse.example.domain.services.EmployeeService;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.domain.services.GroupManagerService;

import java.util.List;

/**
 * Helper class for static functions to determine if a user is a manager.
 */
public final class ManagerHelper {
    private ManagerHelper() {

    }

    /* default */ static boolean isEmployee(final User user, final AccessCourse course,
                                            final EmployeeService employeeService) {
        if (course == null) {
            return false;
        }
        final List<Employee> usercourses = employeeService.getEmployeesByUser(user);
        for (final Employee emp : usercourses) {
            if (emp.getAccessCourse().getId().equals(course.getId())) {
                return true;
            }
        }
        return false;
    }

    /* default */ static boolean isGroupManager(final User user, final AccessGroup group,
                                                final GroupManagerService groupManagerService) {
        if (group == null) {
            return false;
        }
        final List<GroupManager> userGroups = groupManagerService.getGroupManagerByUser(user);
        for (final GroupManager groupManager : userGroups) {
            if (groupManager.getAccessGroup().getId().equals(group.getId())) {
                return true;
            }
        }
        return false;
    }

    /* default */ static boolean isFacultyManager(final User user, final Faculty faculty,
                                                  final FacultyManagerService facultyManagerService) {
        if (faculty == null) {
            return false;
        }
        final List<FacultyManager> userFaculties = facultyManagerService.getFacultyManagersByUser(user);
        for (final FacultyManager facultyManager : userFaculties) {
            if (facultyManager.getFaculty().getId().equals(faculty.getId())) {
                return true;
            }
        }
        return false;
    }
}
