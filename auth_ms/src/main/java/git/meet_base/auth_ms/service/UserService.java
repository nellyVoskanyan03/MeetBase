package git.meet_base.auth_ms.service;

import git.meet_base.auth_ms.dto.UserRegistrationRequest;
import git.meet_base.auth_ms.dto.UserResponse;
import git.meet_base.auth_ms.exception.EmailAlreadyInUseException;
import git.meet_base.auth_ms.model.User;
import git.meet_base.auth_ms.model.UserRole;
import git.meet_base.auth_ms.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Map<UUID, String> getUserEmailsBatch(List<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return userRepository.findEmailsByUserIds(userIds).stream()
                .collect(Collectors.toMap(
                        UserRepository.UserEmailProjection::getId,
                        UserRepository.UserEmailProjection::getEmail
                ));
    }

    @Transactional
    public UserResponse createUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException("The email '" + request.getEmail() + "' is already registered.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() );
        user.setIsActive(true);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCompanyName(request.getCompanyName());


        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser);
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return new UserResponse(user);
    }

    public List<UserResponse> getUsers(UserRole role, Boolean isActive, String companyName) {
        List<User> users = userRepository.findUsersWithFilters(role, companyName, isActive);

        return users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
}