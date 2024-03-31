package kwgh0st.project.todoappbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import kwgh0st.project.todoappbackend.exception.UserNotFoundException;
import kwgh0st.project.todoappbackend.model.User;
import kwgh0st.project.todoappbackend.model.dto.PasswordDTO;
import kwgh0st.project.todoappbackend.model.dto.UserDTO;
import kwgh0st.project.todoappbackend.service.MailService;
import kwgh0st.project.todoappbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private static final String INVALID_PASSWORD = "Invalid password!";
    private static final String EMAIL_CHANGED = "Email changed successfully!";
    private static final String PASSWORD_CHANGED = "Password changed successfully!";
    private static final String PASSWORD_OLD_AND_NEW_SAME = "The new password must be different from the old one!";
    private static final String USER_ACCOUNT_DELETED = "User account deleted! You will be logout.";
    private static final String EMAIL_OLD_AND_NEW_SAME = "The new email must be different from the old one!";

    @GetMapping("/fetch-data/{username}")
    public ResponseEntity<UserDTO> fetchUserData(@PathVariable String username) {
        User user = userService.getUserByUsername(username);

        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(UserDTO
                .builder()
                .id(user.getId())
                .email
                        (user.getEmail())
                .wantTodosNotification(user.isWantTodosNotification())
                .build());
    }


    @PostMapping("/change-email")
    public ResponseEntity<String> changeUserEmail(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        User user = userService.getUserByUsername(userDTO.getUsername());
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserNotFoundException.MESSAGE);

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_PASSWORD);
        }

        if (user.getEmail().equalsIgnoreCase(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(EMAIL_OLD_AND_NEW_SAME);
        }

        userService.changeUserEmail(user, userDTO.getEmail());
        mailService.sendSuccessEmailChangedMessage(request, userDTO.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(EMAIL_CHANGED);
    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changeUserPassword(HttpServletRequest request, @RequestBody PasswordDTO passwordDTO) {
        User user = userService.getUserByEmail(passwordDTO.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserNotFoundException.MESSAGE);
        }

        if (!passwordEncoder.matches(passwordDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_PASSWORD);
        }

        if (passwordEncoder.matches(passwordDTO.getNewPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(PASSWORD_OLD_AND_NEW_SAME);
        }

        userService.changeUserPassword(user, passwordDTO.getNewPassword());
        mailService.sendSuccessPasswordChangeEmail(request, user);

        return ResponseEntity.status(HttpStatus.OK).body(PASSWORD_CHANGED);
    }


    @PostMapping("/delete-account")
    public ResponseEntity<String> deleteUserAccount(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        User user = userService.getUserByUsername(userDTO.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserNotFoundException.MESSAGE);
        }

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_PASSWORD);
        }
        userService.deleteUserAccountByUsername(user);
        mailService.sendSuccessAccountDeletedMessage(request, user);
        return ResponseEntity.status(HttpStatus.OK).body(USER_ACCOUNT_DELETED);
    }
}
