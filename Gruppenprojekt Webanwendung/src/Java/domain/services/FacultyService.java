package gpse.example.domain.services;

import gpse.example.domain.cmds.FacultyCmd;
import gpse.example.domain.entities.Faculty;

import java.util.List;
import java.util.Optional;

/**
 * Service for Faculties.
 */
public interface FacultyService {

    List<Faculty> getAllFaculties();

    Optional<Faculty> getFacultyById(Long id);

    Faculty getFacultyByName(String name) throws NotFoundException;

    Faculty saveFaculty(Faculty faculty);

    Faculty createFaculty(String name);

    Faculty updateFaculty(Long id, FacultyCmd facultycmd);

    Faculty deleteFaculty(Faculty faculty);
}

