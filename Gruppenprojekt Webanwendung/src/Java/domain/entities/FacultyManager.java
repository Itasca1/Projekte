package gpse.example.domain.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * Model representation of an FacultyManager.
 */
@Entity
public class FacultyManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE)
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Faculty faculty;

    protected FacultyManager() {
    }

    /**
     * Constructor for a FacultyManager.
     *
     * @param user    the user
     * @param faculty the faculty
     */
    public FacultyManager(final User user, final Faculty faculty) {
        this.user = user;
        this.faculty = faculty;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Long getId() {
        return id;
    }


    public void setFaculty(final Faculty faculty) {
        this.faculty = faculty;
    }


    public void setId(final Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}

