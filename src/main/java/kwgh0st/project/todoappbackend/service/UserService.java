package kwgh0st.project.todoappbackend.service;

import jakarta.transaction.Transactional;
import kwgh0st.project.todoappbackend.exception.*;
import kwgh0st.project.todoappbackend.model.*;
import kwgh0st.project.todoappbackend.model.dto.NewUserDTO;
import kwgh0st.project.todoappbackend.model.dto.UserDTO;
import kwgh0st.project.todoappbackend.repository.ConfirmRegistrationTokenRepository;
import kwgh0st.project.todoappbackend.repository.PasswordResetTokenRepository;
import kwgh0st.project.todoappbackend.repository.UpdatePropertiesTokenRepository;
import kwgh0st.project.todoappbackend.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
@Getter
@Setter
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ConfirmRegistrationTokenRepository confirmTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UpdatePropertiesTokenRepository updatePropertiesTokenRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User registerNewUserAccount(final UserDTO userDTO) {
        if (emailExists(userDTO.getEmail())) {
            throw new EmailAlreadyExistException(EmailAlreadyExistException.MESSAGE);
        } else if (usernameExists(userDTO.getUsername())) {
            throw new UserAlreadyExistException(UserAlreadyExistException.MESSAGE);
        }

        return userRepository.save(User
                .builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .role(Role.USER)
                .enabled(false)
                .wantTodosNotification(userDTO.isWantTodosNotification())
                .accountNonLocked(true)
                .registrationDate(LocalDate.now())
                .build());
    }

    @Transactional
    public void registerNewAdminAccount(final UserDTO userDTO) {
        if (emailExists(userDTO.getEmail())) {
            throw new EmailAlreadyExistException(EmailAlreadyExistException.MESSAGE);
        } else if (usernameExists(userDTO.getUsername())) {
            throw new UserAlreadyExistException(UserAlreadyExistException.MESSAGE);
        }

        userRepository.save(
                User
                        .builder()
                        .username(userDTO.getUsername())
                        .password(passwordEncoder.encode(userDTO.getPassword()))
                        .email(userDTO.getEmail())
                        .role(Role.ADMIN)
                        .enabled(userDTO.isEnabled())
                        .wantTodosNotification(userDTO.isWantTodosNotification())
                        .accountNonLocked(userDTO.isAccountNonLocked())
                        .registrationDate(LocalDate.now())
                        .build());
    }

    @Transactional
    public User createNewAccountByAdmin(final NewUserDTO newUserDTO) {
        if (emailExists(newUserDTO.getEmail())) {
            throw new EmailAlreadyExistException(EmailAlreadyExistException.MESSAGE);
        }

        return userRepository.save(
                User
                        .builder()
                        .username("")
                        .email(newUserDTO.getEmail())
                        .role(Role.valueOf(newUserDTO.getRole()))
                        .registrationDate(LocalDate.now())
                        .build());

    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
    }

    public User getUserByPasswordResetToken(String resetToken) {
        final PasswordResetToken token = passwordResetTokenRepository.findByToken(resetToken).orElseThrow(() -> new InvalidResetPasswordTokenException(InvalidResetPasswordTokenException.MESSAGE));
        return userRepository.findByPasswordResetToken(token).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUserByWantNotification() {
        return userRepository.findByWantTodosNotificationTrue().orElse(Collections.emptyList());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmailIgnoreCase(email).isPresent();
    }

    public boolean checkPermissions(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(UserNotFoundException.MESSAGE));
        return !passwordEncoder.matches(password, user.getPassword()) || !user.getRole().equals(Role.ADMIN);
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsernameIgnoreCase(username).isPresent();
    }

    @Transactional
    public void createUpdatePropertiesTokenForUser(User newUser) {
        final String token = UUID.randomUUID().toString();
        final UpdatePropertiesToken propToken = new UpdatePropertiesToken(token);
        newUser.setUpdatePropertiesToken(propToken);
        updatePropertiesTokenRepository.save(propToken);
        userRepository.save(newUser);
    }

    @Transactional
    public boolean isValidUpdatePropertiesToken(String token) {
        final UpdatePropertiesToken propToken = updatePropertiesTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundException.MESSAGE));

        if (propToken.isExpired()) return true;

        return userRepository.existsByUpdatePropertiesToken(propToken).isEmpty();
    }

    @Transactional
    public void updateUserPropertiesWithToken(String token, UserDTO userDTO) {
        final UpdatePropertiesToken propToken = updatePropertiesTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundException.MESSAGE));

        final User user = userRepository
                .findByUpdatePropertiesToken(propToken)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.MESSAGE));


        propToken.setExpired(true);
        updatePropertiesTokenRepository.save(propToken);

        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public void createVerificationTokenForUser(final User user) {
        final String token = UUID.randomUUID().toString();
        final VerificationToken myToken = new VerificationToken(token);
        user.setVerificationToken(myToken);
        confirmTokenRepository.save(myToken);
        userRepository.save(user);
    }


    @Transactional
    public void validateVerificationToken(String token) {
        final VerificationToken verificationToken = confirmTokenRepository.findByToken(token).orElseThrow(() -> new InvalidRegistrationTokenException(InvalidRegistrationTokenException.MESSAGE));
        final User user = userRepository.findByVerificationToken(verificationToken).orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundException.MESSAGE));

        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            confirmTokenRepository.delete(verificationToken);
            throw new ExpiredRegistrationTokenException(ExpiredRegistrationTokenException.MESSAGE);
        }

        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        confirmTokenRepository.delete(verificationToken);
    }

    @Transactional
    public void generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = confirmTokenRepository.findByToken(existingVerificationToken).orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundException.MESSAGE));
        vToken.updateToken(UUID.randomUUID().toString());
        confirmTokenRepository.save(vToken);
    }

    @Transactional
    public void createPasswordResetTokenForUser(User user) {

        PasswordResetToken resetToken = user.getPasswordResetToken();

        if (resetToken == null) {
            resetToken = new PasswordResetToken(UUID.randomUUID().toString());
        } else {
            resetToken.updateToken(UUID.randomUUID().toString());
        }
        user.setPasswordResetToken(resetToken);
        userRepository.save(user);
        passwordResetTokenRepository.save(resetToken);

    }

    @Transactional
    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        PasswordResetToken token = user.getPasswordResetToken();
        if (token != null) {
            user.setPasswordResetToken(null);
            passwordResetTokenRepository.delete(token);
        }
    }

    @Transactional
    public void changeUserEmail(User user, String email) {
        user.setEmail(email);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserAccountByUsername(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void deleteUserAccountById(Long id) {
        userRepository.deleteById(id);
    }


    @Transactional
    public void blockUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(UserNotFoundException.MESSAGE));
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

    @Transactional
    public void unlockUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(UserNotFoundException.MESSAGE));
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }

    @Transactional
    public void changeUserRole(Long id, String role) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(UserNotFoundException.MESSAGE));
        user.setRole(Role.valueOf(role));
        userRepository.save(user);
    }
}
