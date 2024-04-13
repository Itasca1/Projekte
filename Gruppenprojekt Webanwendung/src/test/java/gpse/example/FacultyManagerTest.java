package gpse.example;

import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.FacultyManager;
import gpse.example.domain.entities.User;
import gpse.example.domain.repositories.FacultyManagerRepository;
import gpse.example.domain.repositories.FacultyRepository;
import gpse.example.domain.repositories.UserRepository;
import gpse.example.domain.serviceimpl.FacultyManagerServiceImpl;
import gpse.example.domain.serviceimpl.FacultyServiceImpl;
import gpse.example.domain.serviceimpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FacultyManagerTest {
    @Autowired
    private FacultyManagerRepository facultyManagerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    /* default */ void facultyManagerCreation() {
        // Create faculty manager
        final FacultyManagerServiceImpl facultyManagerService = new FacultyManagerServiceImpl(facultyManagerRepository);
        final UserServiceImpl userService = new UserServiceImpl(userRepository, null, null);
        final FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        User user = new User("facultyManagerCreationTestMail", "facultyManagerCreationTestFirstname",
                "facultyManagerCreationTestLastname", new ArrayList<>(), "facultyManagerCreationTestPassword", false ,false);
        user = userService.saveUser(user);
        final Faculty faculty = facultyService.createFaculty("facultyManagerCreationTestFaculty");
        FacultyManager facultyManager = new FacultyManager(user, faculty);
        facultyManager = facultyManagerService.createFacultyManager(facultyManager);

        // Test
        final Optional<FacultyManager> facultyManagerOpt = facultyManagerService.getFacultyManager(String.valueOf(facultyManager.getId()));
        assertThat(facultyManagerOpt.isPresent()).isTrue();
        facultyManager = facultyManagerOpt.get();
        assertThat(facultyManager.getId()).isNotNull();
        assertThat(facultyManager.getFaculty().getId()).isEqualTo(facultyService.getAllFaculties().get(0).getId());
        assertThat(facultyManager.getUser().getId()).isEqualTo(userService.getUsers().get(0).getId());
    }

    @Test
    /* default */ void facultyManagerUpdate() {
        // Create faculty manager
        final FacultyManagerServiceImpl facultyManagerService = new FacultyManagerServiceImpl(facultyManagerRepository);
        final UserServiceImpl userService = new UserServiceImpl(userRepository, null, null);
        final FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        User user = new User("facultyManagerUpdateTestMail", "facultyManagerUpdateTestFirstname",
                "facultyManagerUpdateTestLastname", new ArrayList<>(),  "facultyManagerUpdateTestPassword", false ,false);
        user = userService.saveUser(user);
        final Faculty faculty = facultyService.createFaculty("facultyManagerUpdateTestFaculty");
        FacultyManager facultyManager = new FacultyManager(user, faculty);
        facultyManager = facultyManagerService.createFacultyManager(facultyManager);

        // Update faculty manager
        final Faculty newFaculty = facultyService.createFaculty("facultyManagerUpdatedTestFaculty");
        facultyManager.setFaculty(newFaculty);
        facultyManagerService.updateFacultyManager(String.valueOf(facultyManager.getId()), facultyManager);

        // Test
        final Optional<FacultyManager> updatedFacultyManagerOpt = facultyManagerService.getFacultyManager(String.valueOf(facultyManager.getId()));
        assertThat(updatedFacultyManagerOpt.isPresent()).isTrue();
        final FacultyManager updatedFacultyManager = updatedFacultyManagerOpt.get();
        assertThat(updatedFacultyManager.getId()).isNotNull();
        assertThat(updatedFacultyManager.getFaculty().getId()).isEqualTo(newFaculty.getId());
        assertThat(updatedFacultyManager.getFaculty().getName()).isEqualTo("facultyManagerUpdatedTestFaculty");
        assertThat(updatedFacultyManager.getUser().getId()).isEqualTo(userService.getUsers().get(0).getId());
    }

    @Test
    /* default */ void facultyManagerDelete() {
        // Create faculty manager
        final FacultyManagerServiceImpl facultyManagerService = new FacultyManagerServiceImpl(facultyManagerRepository);
        final UserServiceImpl userService = new UserServiceImpl(userRepository, null, null);
        final FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        User user = new User("facultyManagerDeleteTestMail", "facultyManagerDeleteTestFirstname",
                "facultyManagerDeleteTestLastname", new ArrayList<>(), "facultyManagerDeleteTestPassword", false ,false);
        user = userService.saveUser(user);
        final Faculty faculty = facultyService.createFaculty("facultyManagerUpdateTestFaculty");
        FacultyManager facultyManager = new FacultyManager(user, faculty);
        facultyManager = facultyManagerService.createFacultyManager(facultyManager);

        // Delete faculty manager
        facultyManagerService.deleteFacultyManager(facultyManager);

        // Test
        assertThat(facultyManagerService.getFacultyManager(String.valueOf(facultyManager.getId()))).isEmpty();
        assertThat(facultyService.getFacultyById(faculty.getId())).isPresent();
        assertThat(userService.getUser(String.valueOf(user.getId()))).isPresent();
    }
}
