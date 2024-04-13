package gpse.example.domain.cmds;

import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.User;

/**
 * Command object for FacultyManagers.
 * Encapsulates all relevant information needed by api action.
 */
public class FacultyManagerCmd {

    private User user;
    private Long id;
    private Faculty faculty;
    private Long userID;
    private Long facultyID;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(final Long userID) {
        this.userID = userID;
    }

    public Long getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(final Long facultyID) {
        this.facultyID = facultyID;
    }

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

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(final Faculty faculty) {
        this.faculty = faculty;
    }
}

