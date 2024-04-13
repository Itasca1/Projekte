package gpse.example.domain.repositories;

import gpse.example.domain.entities.HTML;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the html objects.
 */
public interface HTMLRepository extends CrudRepository<HTML, Long> {
    HTML findByName(String name);
}
