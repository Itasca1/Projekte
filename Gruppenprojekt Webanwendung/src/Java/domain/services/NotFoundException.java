package gpse.example.domain.services;

import java.io.Serial;

/**
 * Not found Exception.
 * Used when Object not in DB
 */
public class NotFoundException extends Exception {
    @Serial
    private static final long serialVersionUID = -8018887752220992062L;

    public NotFoundException(final String message) {
        super(message);
    }
}

