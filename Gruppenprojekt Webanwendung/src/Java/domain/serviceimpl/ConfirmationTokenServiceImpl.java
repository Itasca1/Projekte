package gpse.example.domain.serviceimpl;

import gpse.example.domain.entities.ConfirmationTokens;
import gpse.example.domain.repositories.ConfirmationTokenRepository;
import gpse.example.domain.services.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of confirmationTokensService.
 */
@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenServiceImpl(final ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public ConfirmationTokens findToken(final String confirmationToken) {
        return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    }

    @Override
    public ConfirmationTokens saveToken(final ConfirmationTokens confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public List<ConfirmationTokens> getTokens() {
        final List<ConfirmationTokens> tokens = new ArrayList<>();
        confirmationTokenRepository.findAll().forEach(tokens::add);
        return tokens;
    }
}
