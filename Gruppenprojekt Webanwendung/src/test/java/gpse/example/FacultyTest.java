package gpse.example;

import gpse.example.domain.cmds.FacultyCmd;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.repositories.FacultyRepository;
import gpse.example.domain.serviceimpl.FacultyServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FacultyTest {
    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    /* default */ void facultyCreation() {
        // Create faculty
        final FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        Faculty faculty = facultyService.createFaculty("facultyCreationTestFaculty");

        // Test
        final Optional<Faculty> facultyOpt = facultyService.getFacultyById(faculty.getId());
        assertThat(facultyOpt.isPresent()).isTrue();
        faculty = facultyOpt.get();
        assertThat(faculty.getId()).isNotNull();
        assertThat(faculty.getName()).isEqualTo("facultyCreationTestFaculty");
    }

    @Test
    /* default */ void facultyUpdate() {
        // Create faculty
        final FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        final Faculty faculty = facultyService.createFaculty("facultyUpdateTestFaculty");

        // Update faculty
        final FacultyCmd facultyCmd = new FacultyCmd();
        facultyCmd.setName("facultyUpdatedTestFaculty");
        facultyService.updateFaculty(faculty.getId(), facultyCmd);

        // Test
        final Optional<Faculty> updatedFacultyOpt = facultyService.getFacultyById(faculty.getId());
        assertThat(updatedFacultyOpt.isPresent()).isTrue();
        final Faculty updatedFaculty = updatedFacultyOpt.get();
        assertThat(updatedFaculty.getName()).isEqualTo("facultyUpdatedTestFaculty");
    }

    @Test
    /* default */ void facultyDelete() {
        // Create faculty
        final FacultyServiceImpl facultyService = new FacultyServiceImpl(facultyRepository);
        final Faculty faculty = facultyService.createFaculty("facultyDeleteTestFaculty");

        // Delete faculty
        facultyService.deleteFaculty(faculty);

        // Test
        assertThat(facultyService.getFacultyById(faculty.getId())).isEmpty();
    }
}
