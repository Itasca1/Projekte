package gpse.example.domain.repositories;

import gpse.example.domain.entities.Review;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the review objects.
 */
public interface ReviewRepository extends CrudRepository<Review, Long> {
}
