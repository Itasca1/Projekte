package gpse.example.domain.serviceimpl;

import gpse.example.domain.cmds.FacultyCmd;
import gpse.example.domain.repositories.FacultyRepository;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.services.FacultyService;
import gpse.example.domain.services.NotFoundException;
import gpse.example.web.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Impl of facultyservice.
 */
@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(final FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Optional<Faculty> getFacultyById(final Long id) {
        return facultyRepository.findById(id);
    }

    @Override
    public List<Faculty> getAllFaculties() {
        final List<Faculty> faculties = new ArrayList<>();
        facultyRepository.findAll().forEach(faculties::add);
        return faculties;
    }

    @Override
    public Faculty getFacultyByName(final String name) throws NotFoundException {
        final Faculty faculty = facultyRepository.findByName(name);
        if (faculty == null) {
            throw new NotFoundException("Faculty not found");
        } else {
            return faculty;
        }
    }

    @Override
    public Faculty saveFaculty(final Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty createFaculty(final String name) {
        final Faculty faculty = new Faculty(name);
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty updateFaculty(final Long id, final FacultyCmd facultyCmd) {
        final Faculty faculty = facultyRepository.findById(id).orElseThrow(BadRequestException::new);
        faculty.setName(facultyCmd.getName());
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty deleteFaculty(final Faculty faculty) {
        facultyRepository.delete(faculty);
        return faculty;
    }
}

