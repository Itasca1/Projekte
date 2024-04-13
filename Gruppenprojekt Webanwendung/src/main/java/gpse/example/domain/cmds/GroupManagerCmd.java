package gpse.example.domain.cmds;

import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.User;

/**
 * Command object for GroupManagers.
 * Encapsulates all relevant information needed by api action.
 */
public class GroupManagerCmd {
    private Long id;
    private User user;
    private AccessGroup accessGroup;
    private Long userID;
    private Long groupID;

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public AccessGroup getAccessGroup() {
        return accessGroup;
    }

    public void setAccessGroup(final AccessGroup accessGroup) {
        this.accessGroup = accessGroup;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(final Long userID) {
        this.userID = userID;
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(final Long groupID) {
        this.groupID = groupID;
    }
}

