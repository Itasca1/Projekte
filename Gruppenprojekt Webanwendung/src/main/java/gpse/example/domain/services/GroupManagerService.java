package gpse.example.domain.services;



import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.GroupManager;
import gpse.example.domain.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Service for GroupManager.
 */
public interface GroupManagerService {

    List<GroupManager> getGroupManagers();
    List<GroupManager> getGroupManagerByGroup(AccessGroup accessGroup);
    GroupManager loadGroupManagerById(Long id);
    GroupManager saveGroupManager(GroupManager groupManager);
    GroupManager createGroupManager(GroupManager groupManager);
    GroupManager updateGroupManager(String id, GroupManager groupManager);
    void deleteGroupManager(GroupManager groupManager);
    Optional<GroupManager> getGroupManager(String id);
    GroupManager searchForGroupManager(User user, AccessGroup group);
    List<GroupManager> getGroupManagerByUser(User user);

}

