package gpse.example;

import gpse.example.domain.entities.AccessGroup;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.repositories.FacultyRepository;
import gpse.example.domain.repositories.GroupRepository;
import gpse.example.domain.serviceimpl.AccessGroupServiceImpl;
import gpse.example.domain.serviceimpl.FacultyServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GroupTest {
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private GroupRepository groupRepository;

    @Test
    /* default */ void groupCreation() {
        // Create group
        final AccessGroupServiceImpl groupService = new AccessGroupServiceImpl(groupRepository);
        final FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        Faculty faculty = new Faculty("groupCreationTestFaculty");
        AccessGroup group = groupService.createAccessGroup("groupCreationTestGroup",
                "groupCreationTestSemester", faculty);
        facultyService.saveFaculty(faculty);

        // Test
        faculty = facultyService.getAllFaculties().get(0);
        final Optional<AccessGroup> groupOpt = groupService.getGroupById(group.getId());
        assertThat(groupOpt.isPresent()).isTrue();
        group = groupOpt.get();
        assertThat(group.getId()).isNotNull();
        assertThat(group.getName()).isEqualTo("groupCreationTestGroup");
        assertThat(group.getFaculty()).isEqualTo(faculty);
    }

    @Test
    /* default */ void groupUpdate() {
        // Create group
        final AccessGroupServiceImpl groupService = new AccessGroupServiceImpl(groupRepository);
        final FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        final Faculty faculty = new Faculty("groupUpdateTestFaculty");
        final AccessGroup group = groupService.createAccessGroup("groupUpdateTestGroup",
                "groupUpdateTestSemester", faculty);
        facultyService.saveFaculty(faculty);

        // Update group
        group.setName("groupUpdatedTestGroup");
        group.setSemester("groupUpdatedTestSemester");
        groupService.updateAccessGroup(String.valueOf(group.getId()), group);

        // Test
        final Optional<AccessGroup> updatedGroupOpt = groupService.getGroupById(group.getId());
        assertThat(updatedGroupOpt.isPresent()).isTrue();
        final AccessGroup updatedGroup = updatedGroupOpt.get();
        assertThat(updatedGroup.getName()).isEqualTo("groupUpdatedTestGroup");
        assertThat(updatedGroup.getSemester()).isEqualTo("groupUpdatedTestSemester");
        assertThat(updatedGroup.getFaculty()).isEqualTo(facultyService.getAllFaculties().get(0));
    }

    @Test
    /* default */ void groupDelete() {
        // Create group
        final AccessGroupServiceImpl groupService = new AccessGroupServiceImpl(groupRepository);
        final FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        final Faculty faculty = new Faculty("groupDeleteTestFaculty");
        final AccessGroup group = groupService.createAccessGroup("groupDeleteTestGroup",
                "groupDeleteTestSemester", faculty);
        facultyService.saveFaculty(faculty);

        // Delete group
        groupService.deleteGroup(group);

        // Test
        assertThat(groupService.getGroupById(group.getId())).isEmpty();
    }
}
