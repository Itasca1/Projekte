package gpse.example.domain.services;

import gpse.example.domain.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;
import java.util.Optional;

/**
 * Service for User.
 */
public interface UserService extends UserDetailsService {
    List<User> getUsers();
    User loadUserById(Long id);
    User loadUserByMail(String email);
    User saveUser(User user);
    User createUser(User user, String... roles);
    User updateUser(String id, User user);
    User updateUserInfo(User user);
    User updateUserInformaton(User user);
    void deleteUser(User user);
    Optional<User> getUser(String id);
}

