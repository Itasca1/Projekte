package gpse.example.domain.cmds;

import gpse.example.domain.entities.AccessCourse;
import gpse.example.domain.entities.User;

/**
 * Command object for Employees.
 * Encapsulates all relevant information needed by api action.
 */
public class EmployeeCmd {
    private Long id;
    private User user;
    private AccessCourse accessCourse;
    private Long userID;
    private Long courseID;

    public void setUser(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setAccessCourse(final AccessCourse accessCourse) {
        this.accessCourse = accessCourse;
    }

    public AccessCourse getAccessCourse() {
        return accessCourse;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(final Long userID) {
        this.userID = userID;
    }

    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(final Long courseID) {
        this.courseID = courseID;
    }
}

