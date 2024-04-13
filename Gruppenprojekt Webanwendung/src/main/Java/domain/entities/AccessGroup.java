package gpse.example.domain.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * Model representation of an AccessGroup.
 */
@Entity
public class AccessGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private String semester;

    @ManyToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Faculty faculty;

    protected AccessGroup() {
    }

    /**
     * Constructor for Group.
     *
     * @param name     name of AccessGroup
     * @param semester semester of AccessGroup
     * @param faculty  faculty of AccessGroup
     */
    public AccessGroup(final String name, final String semester, final Faculty faculty) {
        this.name = name;
        this.semester = semester;
        this.faculty = faculty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

