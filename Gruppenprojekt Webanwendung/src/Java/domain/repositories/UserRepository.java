package gpse.example.domain.repositories;

import gpse.example.domain.entities.User;
import org.springframework.data.repository.CrudRepository;
/**
 * Repository for User.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
