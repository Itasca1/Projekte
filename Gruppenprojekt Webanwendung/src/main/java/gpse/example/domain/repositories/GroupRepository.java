package gpse.example.domain.repositories;

import gpse.example.domain.entities.AccessGroup;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for Groups.
 */
public interface GroupRepository extends CrudRepository<AccessGroup, Long> {
    AccessGroup findByName(String name);
}

