package gpse.example.domain.repositories;

import gpse.example.domain.entities.Faculty;
import org.springframework.data.repository.CrudRepository;
/**
 * Repository for the Faculties.
 */
public interface FacultyRepository extends CrudRepository<Faculty, Long> {
    Faculty findByName(String name);
}

