package gpse.example.domain.repositories;

import gpse.example.domain.entities.AccessCourse;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the courses.
 */
public interface CourseRepository extends CrudRepository<AccessCourse, Long> {
    AccessCourse findByName(String name);
}

