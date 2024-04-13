package gpse.example.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.io.Serial;
import java.util.Collection;
import java.util.List;

/**
 * Model representation of an User.
 */
@Entity
public class User implements UserDetails {
    @Serial
    private static final long serialVersionUID = 0L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String email;
    @Column
    private String firstname;
    @Column
    private String lastname;

    @Column
    private Boolean visible;

    @JoinColumn
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @Fetch(FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<AccessCourse> recentlyVisitedCourses = new ArrayList<>();
    @Column
    private boolean menuOpen;
    @Column
    @JsonIgnore
    private String password;
    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    private boolean userEnabled;

    private Boolean userScoreDisabled;

    protected User() {

    }

    /**
     * Constructor for User.
     * @param email Email of User
     * @param firstname Firstname of User
     * @param lastname Lastname of User
     * @param recentlyVisitedCourses a list of courses the user visited recently
     * @param menuOpen wether the user has opened or closed the menu
     * @param password password of User
     * @param visible visibility of the User
     */
    public User(final String email, final String firstname, final String lastname,
                final List<AccessCourse> recentlyVisitedCourses, final String password,
                final boolean visible, final boolean menuOpen) {

        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.recentlyVisitedCourses = recentlyVisitedCourses;
        this.menuOpen = menuOpen;
        this.password = password;
        this.userEnabled = false;
        this.visible = visible;
        this.userScoreDisabled = visible;
    }
    @SuppressWarnings("CPD-START")
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String mail) {
        this.email = mail;
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

    public void setPassword(final String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(final List<String> roles) {
        this.roles = roles;
    }

    public boolean isUserEnabled() {
        return userEnabled;
    }

    public void setUserEnabled(final boolean userEnabled) {
        this.userEnabled = userEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(roles.toArray(new String[0]));
    }

    /**
     * Method to add Roles for a user.
     *
     * @param role role that should be added to a user
     */
    public void addRole(final String role) {
        if (roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userEnabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Boolean isUserScoreDisabled() {
        return userScoreDisabled;
    }

    public void setUserScoreDisabled(final Boolean userScoreDisabled) {
        this.userScoreDisabled = userScoreDisabled;
    }

    @SuppressWarnings("CPD-END")
    @Override
    public String toString() {
        return "Email: " + this.email + " Firstname: " + this.firstname + " Lastname: " + this.lastname;
    }
}

