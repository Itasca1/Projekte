package gpse.example.domain.serviceimpl;

import gpse.example.domain.entities.FacultyManager;
import gpse.example.domain.entities.Faculty;
import gpse.example.domain.entities.User;
import gpse.example.domain.repositories.FacultyManagerRepository;
import gpse.example.domain.services.FacultyManagerService;
import gpse.example.web.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of {@link FacultyManagerService} needed to load employees.
 */
@Service
public class FacultyManagerServiceImpl implements FacultyManagerService {

    private final FacultyManagerRepository facultyManagerRepository;

    @Autowired
    public FacultyManagerServiceImpl(final FacultyManagerRepository facultyManagerRepository) {
        this.facultyManagerRepository = facultyManagerRepository;
    }

    @Override
    public List<FacultyManager> getFacultyManagers() {
        final List<FacultyManager> facultyManagers = new ArrayList<>();
        facultyManagerRepository.findAll().forEach(facultyManagers::add);
        return facultyManagers;
    }

    @Override
    public FacultyManager loadFacultyManagerById(final Long id) {
        return facultyManagerRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("FacultyManager with id " + id + " not found."));
    }

    @Override
    public FacultyManager saveFacultyManager(final FacultyManager facultyManager) {
        return facultyManagerRepository.save(facultyManager);
    }


    @Override
    public FacultyManager createFacultyManager(final FacultyManager tmpFacultyManager) {
        final FacultyManager facultyManager = new FacultyManager(tmpFacultyManager.getUser(),
                tmpFacultyManager.getFaculty());
        return facultyManagerRepository.save(facultyManager);
    }

    @Override
    public FacultyManager updateFacultyManager(final String id, final FacultyManager tempFacultyManager) {
        final Long facultyManagerID = Long.valueOf(id);
        final FacultyManager facultyManager = facultyManagerRepository.findById(facultyManagerID)
                .orElseThrow(BadRequestException::new);

        facultyManager.setUser(tempFacultyManager.getUser());
        facultyManager.setFaculty(tempFacultyManager.getFaculty());

        return facultyManagerRepository.save(facultyManager);
    }

    @Override
    public void deleteFacultyManager(final FacultyManager facultyManager) {
        facultyManagerRepository.delete(facultyManager);
    }

    @Override
    public Optional<FacultyManager> getFacultyManager(final String id) {
        final Long facultyManagerId = Long.valueOf(id);
        return facultyManagerRepository.findById(facultyManagerId);
    }

    @Override
    public List<FacultyManager> getFacultyManagerByFaculty(final Faculty faculty) {
        final List<FacultyManager> facultyManagers = new ArrayList<>();
        final List<FacultyManager> facultyMangersFinal = new ArrayList<>();
        facultyManagerRepository.findAll().forEach(facultyManagers::add);
        for (final FacultyManager employee : facultyManagers
        ) {
            if (employee.getFaculty() == faculty) {
                facultyMangersFinal.add(employee);
            }
        }
        return facultyMangersFinal;
    }

    @Override
    public List<FacultyManager> getFacultyManagersByUser(final User user) {
        final List<FacultyManager> facultyManagers = new ArrayList<>();
        final List<FacultyManager> facultyManagersFinal = new ArrayList<>();
        facultyManagerRepository.findAll().forEach(facultyManagers::add);
        boolean userfound = false;
        for (final FacultyManager facultyManager : facultyManagers) {
            if (Objects.equals(facultyManager.getUser().getId(), user.getId())) {
                facultyManagersFinal.add(facultyManager);
                userfound = true;
            }
        }
        if (userfound) {
            return facultyManagersFinal;
        } else {
            return new ArrayList<>();
            //throw new UsernameNotFoundException("no Employee found");
        }

    }

    @Override
    public FacultyManager searchForFacultyManager(final User user, final Faculty faculty) {
        try {
            final List<FacultyManager> facultyManagerList = getFacultyManagersByUser(user);
            for (final FacultyManager facultyManager : facultyManagerList
            ) {
                if (Objects.equals(facultyManager.getFaculty().getName(), faculty.getName())) {
                    return facultyManager;
                }
            }
            return null;
        } catch (UsernameNotFoundException e) {
            return null;
        }

    }
}

