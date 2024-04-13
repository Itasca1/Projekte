package gpse.example.domain.serviceimpl;

import gpse.example.domain.entities.ConfirmationTokens;
import gpse.example.domain.entities.Email;
import gpse.example.domain.entities.User;
import gpse.example.domain.repositories.UserRepository;
import gpse.example.domain.services.ConfirmationTokenService;
import gpse.example.domain.services.EmailService;
import gpse.example.domain.services.UserService;
import gpse.example.security.JwtAuthorizationFilter;
import gpse.example.web.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Impl of UserService.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    /**
     * Constructor for UserServiceImpl.
     *
     * @param userRepository           to access Users
     * @param confirmationTokenService to access functions with confirmationTokens
     * @param emailService             to access functions with emails
     */
    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final ConfirmationTokenService confirmationTokenService, final EmailService emailService) {
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
    }

    @Override
    public List<User> getUsers() {
        final List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User loadUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found."));
    }

    @Override
    public User loadUserByMail(final String email) {
        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with mail '%s' not found.", email));

        } else {
            return user;
        }
    }

    @Override
    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    @Override
    public User createUser(final User user, final String... roles) {
        final String hashedPassword = encoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        for (final String role : roles) {
            user.addRole(role);
        }
        user.isEnabled();
        userRepository.save(user);
        user.setUserEnabled(true);
        final ConfirmationTokens confirmationToken = new ConfirmationTokens(user);
        //confirmationTokenService.save(confirmationToken);
        confirmationTokenService.saveToken(confirmationToken);
        final String message = "To confirm your account, please click here : "
                + "http://localhost:8080/verifyAccount/" + confirmationToken.getConfirmationToken();
        final Email email = new Email("Account Verification", message, "", user.getEmail());
        try {
            emailService.sendMail(email);
        } catch (MessagingException e) {
            LOG.error(e.toString());
        }
        return user;
    }

    @Override
    public User updateUser(final String id, final User tempUser) {
        final Long userId = Long.valueOf(id);
        final User user = userRepository.findById(userId)
                .orElseThrow(BadRequestException::new);

        user.setEmail(tempUser.getEmail());
        user.setFirstname(tempUser.getFirstname());
        user.setLastname(tempUser.getLastname());
        user.setRecentlyVisitedCourses(tempUser.getRecentlyVisitedCourses());
        user.setMenuOpen(tempUser.isMenuOpen());
        user.setPassword(tempUser.getPassword());
        user.setVisible(tempUser.getVisible());

        return userRepository.save(user);
    }

    @Override
    public User updateUserInformaton(final User changedUser) {
        final User user = userRepository.findById(changedUser.getId())
                .orElseThrow(BadRequestException::new);
        user.setEmail(changedUser.getEmail());
        user.setFirstname(changedUser.getFirstname());
        user.setLastname(changedUser.getLastname());
        user.setUserScoreDisabled(changedUser.getVisible());
        user.setVisible(changedUser.getVisible());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(final User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> getUser(final String id) {
        final Long userId = Long.valueOf(id);
        return userRepository.findById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(final String email) {
        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email '%s' was not found.", email));
        } else {
            return user;
        }
    }

    @Override
    public User updateUserInfo(final User tempUser) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails authenticatedUser = loadUserByUsername(auth.getName());
        final String email = authenticatedUser.getUsername();
        final User user = userRepository.findByEmail(email);
        user.setEmail(tempUser.getEmail());
        user.setFirstname(tempUser.getFirstname());
        user.setLastname(tempUser.getLastname());
        user.setRecentlyVisitedCourses(tempUser.getRecentlyVisitedCourses());
        user.setVisible(tempUser.getVisible());
        user.setMenuOpen(tempUser.isMenuOpen());
        user.setUserScoreDisabled(tempUser.getVisible());
        return userRepository.save(user);
    }
}

