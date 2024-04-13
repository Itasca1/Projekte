package gpse.example.domain.repositories;

import gpse.example.domain.entities.GroupManager;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for GroupManager.
 */
public interface GroupManagerRepository extends CrudRepository<GroupManager, Long> {
}

