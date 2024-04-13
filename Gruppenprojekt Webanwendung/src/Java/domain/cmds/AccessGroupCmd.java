package gpse.example.domain.cmds;

import gpse.example.domain.entities.Faculty;

/**
 * Command object for groups.
 * Encapsulates all relevant information needed by api action.
 */
public class AccessGroupCmd {
    private String name;
    private String semester;
    private Long facultyID;
    private Faculty faculty;

    public Long getFacultyID() {
        return facultyID;
    }

    public void setFacultyID(final Long facultyID) {
        this.facultyID = facultyID;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(final String semester) {
        this.semester = semester;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(final Faculty faculty) {
        this.faculty = faculty;
    }
}

