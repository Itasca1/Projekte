package gpse.example.domain.repositories;

import gpse.example.domain.entities.FacultyManager;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for FacultyManager.
 */
public interface FacultyManagerRepository extends CrudRepository<FacultyManager, Long> {
}

