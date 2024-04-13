package gpse.example.domain.cmds;

import gpse.example.domain.entities.AccessCourse;

import java.util.ArrayList;
import java.util.List;

/**
 * Command object for User.
 * Encapsulates all relevant information needed by api action.
 */
public class UserCmd {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private List<AccessCourse> recentlyVisitedCourses = new ArrayList<>();
    private boolean menuOpen;
    private String password;
    private String userEnabled;
    private Boolean userScoreDisabled;
    private Boolean visible;

    @SuppressWarnings("CPD-START")
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(final Boolean visible) {
        this.visible = visible;
    }

    public List<AccessCourse> getRecentlyVisitedCourses() {
        return recentlyVisitedCourses;
    }

    public void setRecentlyVisitedCourses(final List<AccessCourse> recentlyVisitedCourses) {
        this.recentlyVisitedCourses = recentlyVisitedCourses;
    }

    public boolean isMenuOpen() {
        return menuOpen;
    }

    public void setMenuOpen(final boolean menuOpen) {
        this.menuOpen = menuOpen;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUserEnabled() {
        return userEnabled;
    }
    @SuppressWarnings("CPD-END")
    public void setUserEnabled(final String userEnabled) {
        this.userEnabled = userEnabled;
    }

    public Boolean isUserScoreDisabled() {
        return userScoreDisabled;
    }

    public void setUserScoreDisabled(final Boolean userScoreDisabled) {
        this.userScoreDisabled = userScoreDisabled;
    }
}
