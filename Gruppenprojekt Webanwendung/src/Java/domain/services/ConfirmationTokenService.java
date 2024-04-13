package gpse.example.domain.services;

import gpse.example.domain.entities.ConfirmationTokens;

import java.util.List;

/**
 * Service for confirmationTokens.
 */
public interface ConfirmationTokenService {
    ConfirmationTokens findToken(String confirmationToken);
    ConfirmationTokens saveToken(ConfirmationTokens confirmationToken);
    List<ConfirmationTokens> getTokens();
}
