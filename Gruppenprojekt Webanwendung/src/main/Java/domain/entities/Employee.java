package gpse.example.domain.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * Model representation of an Employee.
 */
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn
    private User user;
    @ManyToOne(fetch = FetchType.EAGER,
               cascade = CascadeType.MERGE)
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AccessCourse accessCourse;

    protected Employee() {
    }

    /**
     * Constructor for a Employee.
     * @param user the user
     * @param accessCourse the course
     */
    public Employee(final User user, final AccessCourse accessCourse) {
        this.user = user;
        this.accessCourse = accessCourse;
    }

    /*
    Getter and Setter Methods.
     */
    public Long getId() {
        return id;
    }

    public AccessCourse getAccessCourse() {
        return accessCourse;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setAccessCourse(final AccessCourse accessCourse) {
        this.accessCourse = accessCourse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}

