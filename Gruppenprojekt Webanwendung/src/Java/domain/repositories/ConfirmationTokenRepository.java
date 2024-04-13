package gpse.example.domain.repositories;

import gpse.example.domain.entities.ConfirmationTokens;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the confirmationTokens.
 */
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationTokens, Long> {
    ConfirmationTokens findByConfirmationToken(String confirmationToken);
}
