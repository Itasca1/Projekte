package gpse.example.domain.services;
import java.io.Serial;

/**
 * Not found Exception.
 * Used when User not authorized
 */
public class NoAuthorizationException extends  Exception {
    @Serial
    private static final long serialVersionUID = -8018887752220992062L;

    public NoAuthorizationException(final String message) {
        super(message);
    }
}

