package gpse.example.domain.services;

import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.FacultyManager;
import gpse.example.domain.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * Service for FacultyManager.
 */
public interface FacultyManagerService {

    List<FacultyManager> getFacultyManagers();
    List<FacultyManager> getFacultyManagerByFaculty(Faculty faculty);
    FacultyManager loadFacultyManagerById(Long id);
    FacultyManager saveFacultyManager(FacultyManager facultyManager);
    FacultyManager createFacultyManager(FacultyManager facultyManager);
    FacultyManager updateFacultyManager(String id, FacultyManager facultyManager);
    void deleteFacultyManager(FacultyManager facultyManager);
    Optional<FacultyManager> getFacultyManager(String id);
    List<FacultyManager> getFacultyManagersByUser(User user);
    FacultyManager searchForFacultyManager(User user, Faculty faculty);
}

