package gpse.example.domain.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * Model representation of an GroupManager.
 */
@Entity
public class GroupManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE)
    @JoinColumn
    private User user;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE)
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AccessGroup accessGroup;

    protected GroupManager() {
    }

    /**
     * Constructor for a GroupManager.
     *
     * @param user        the user
     * @param accessGroup the group
     */
    public GroupManager(final User user, final AccessGroup accessGroup) {
        this.user = user;
        this.accessGroup = accessGroup;
    }

    public AccessGroup getAccessGroup() {
        return accessGroup;
    }


    public void setId(final Long id) {
        this.id = id;
    }


    public void setAccessGroup(final AccessGroup accessGroup) {
        this.accessGroup = accessGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }
}

