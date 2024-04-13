package gpse.example;

import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.GroupManager;
import gpse.example.domain.entities.User;
import gpse.example.domain.repositories.GroupManagerRepository;
import gpse.example.domain.repositories.GroupRepository;
import gpse.example.domain.repositories.UserRepository;
import gpse.example.domain.serviceimpl.AccessGroupServiceImpl;
import gpse.example.domain.serviceimpl.GroupManagerServiceImpl;
import gpse.example.domain.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GroupManagerTest {
    @Autowired
    private GroupManagerRepository groupManagerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    /* default */ void groupManagerCreation() {
        // Create group manager
        final GroupManagerServiceImpl groupManagerService = new GroupManagerServiceImpl(groupManagerRepository);
        final UserServiceImpl userService = new UserServiceImpl(userRepository, null, null);
        final AccessGroupServiceImpl groupService = new AccessGroupServiceImpl(groupRepository);
        User user = new User("groupManagerCreationTestMail", "groupManagerCreationTestFirstname",
                "groupManagerCreationTestLastname", new ArrayList<>(), "groupManagerCreationTestPassword", false, false);
        user = userService.saveUser(user);
        final AccessGroup faculty = groupService.createAccessGroup("groupManagerCreationTestFaculty", "groupManagerCreationTestSemester", null);
        GroupManager groupManager = new GroupManager(user, faculty);
        groupManager = groupManagerService.createGroupManager(groupManager);

        // Test
        final Optional<GroupManager> groupManagerOpt = groupManagerService.getGroupManager(String.valueOf(groupManager.getId()));
        assertThat(groupManagerOpt.isPresent()).isTrue();
        groupManager = groupManagerOpt.get();
        assertThat(groupManager.getId()).isNotNull();
        assertThat(groupManager.getAccessGroup().getId()).isEqualTo(groupService.getAllGroups().get(0).getId());
        assertThat(groupManager.getUser().getId()).isEqualTo(userService.getUsers().get(0).getId());
    }

    @Test
    /* default */ void groupManagerUpdate() {
        // Create group manager
        final GroupManagerServiceImpl groupManagerService = new GroupManagerServiceImpl(groupManagerRepository);
        final UserServiceImpl userService = new UserServiceImpl(userRepository, null, null);
        final AccessGroupServiceImpl groupService = new AccessGroupServiceImpl(groupRepository);
        User user = new User("groupManagerUpdateTestMail", "groupManagerUpdateTestFirstname",
                "groupManagerUpdateTestLastname", new ArrayList<>(),  "groupManagerUpdateTestPassword", false, false);
        user = userService.saveUser(user);
        final AccessGroup group = groupService.createAccessGroup("groupManagerUpdateTestGroup", "groupManagerUpdateSemester", null);
        GroupManager groupManager = new GroupManager(user, group);
        groupManager = groupManagerService.createGroupManager(groupManager);

        // Update group manager
        final AccessGroup newGroup = groupService.createAccessGroup("groupManagerUpdatedTestGroup", "groupManagerUpdatedTestSemester", null);
        groupManager.setAccessGroup(newGroup);
        groupManagerService.updateGroupManager(String.valueOf(groupManager.getId()), groupManager);

        // Test
        final Optional<GroupManager> updatedGroupManagerOpt = groupManagerService.getGroupManager(String.valueOf(groupManager.getId()));
        assertThat(updatedGroupManagerOpt.isPresent()).isTrue();
        final GroupManager updatedGroupManager = updatedGroupManagerOpt.get();
        assertThat(updatedGroupManager.getId()).isNotNull();
        assertThat(updatedGroupManager.getAccessGroup().getId()).isEqualTo(newGroup.getId());
        assertThat(updatedGroupManager.getAccessGroup().getName()).isEqualTo("groupManagerUpdatedTestGroup");
        assertThat(updatedGroupManager.getUser().getId()).isEqualTo(userService.getUsers().get(0).getId());
    }

    @Test
    /* default */ void groupManagerDelete() {
        // Create group manager
        final GroupManagerServiceImpl groupManagerService = new GroupManagerServiceImpl(groupManagerRepository);
        final UserServiceImpl userService = new UserServiceImpl(userRepository, null, null);
        final AccessGroupServiceImpl groupService = new AccessGroupServiceImpl(groupRepository);
        User user = new User("groupManagerDeleteTestMail", "groupManagerDeleteTestFirstname",
                "groupManagerDeleteTestLastname", new ArrayList<>(),  "groupManagerDeleteTestPassword", false, false);
        user = userService.saveUser(user);
        final AccessGroup group = groupService.createAccessGroup("groupManagerDeleteTestGroup", "groupManagerDeleteSemester", null);
        GroupManager groupManager = new GroupManager(user, group);
        groupManager = groupManagerService.createGroupManager(groupManager);

        // Delete group manager
        groupManagerService.deleteGroupManager(groupManager);

        // Test
        assertThat(groupManagerService.getGroupManager(String.valueOf(groupManager.getId()))).isEmpty();
        assertThat(groupService.getGroupById(group.getId())).isPresent();
        assertThat(userService.getUser(String.valueOf(user.getId()))).isPresent();
    }
}
