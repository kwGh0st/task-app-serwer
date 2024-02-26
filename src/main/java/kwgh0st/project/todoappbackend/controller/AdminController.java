package kwgh0st.project.todoappbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import kwgh0st.project.todoappbackend.exception.EmailAlreadyExistException;
import kwgh0st.project.todoappbackend.model.Role;
import kwgh0st.project.todoappbackend.model.User;
import kwgh0st.project.todoappbackend.model.dto.NewUserDTO;
import kwgh0st.project.todoappbackend.model.dto.RoleDTO;
import kwgh0st.project.todoappbackend.model.dto.UserDTO;
import kwgh0st.project.todoappbackend.service.MailService;
import kwgh0st.project.todoappbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin-panel")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final MailService mailService;
    private static final String USER_DELETED = "User account deleted successfully";
    private static final String USER_BLOCKED = "User account blocked successfully";
    private static final String USER_UNLOCK = "User account is now unlocked!";
    private static final String ROLE_CHANGED = "User role successfully updated!";
    private static final String FORBIDDEN_OPERATION = "You don't have permission for this operation! (Wrong admin password!)";
    private static final String NEW_USER_CREATED = "New user created.";

    @GetMapping("/get-all-users")
    public List<UserDTO> getAllUsers() {

        return userService
                .getAllUsers()
                .stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/get-user/{id}")
    public UserDTO getUser(@PathVariable Long id) {

        return convertToUserDto(userService.getUserById(id));
    }

    @PutMapping("/block-user/{id}")
    public ResponseEntity<String> blockUser(HttpServletRequest request, @PathVariable Long id) {
        try {
            userService.blockUserById(id);
            mailService.sendBlockedAccountMessage(request, userService.getUserById(id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok(USER_BLOCKED);
    }


    @PostMapping("/create-new-user")
    public ResponseEntity<String> createNewUser(HttpServletRequest request, @RequestBody NewUserDTO newUserDTO) {
        if (userService.checkPermissions(newUserDTO.getAdminId(), newUserDTO.getAdminPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(FORBIDDEN_OPERATION);
        }

        try {
            User newUser = userService.createNewAccountByAdmin(newUserDTO);
            userService.createUpdatePropertiesTokenForUser(newUser);
            mailService.sendAccountCreatedByAdminMessage(request, newUser);
        } catch (EmailAlreadyExistException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok(NEW_USER_CREATED);
    }



    @PutMapping("/unlock-user/{id}")
    public ResponseEntity<String> unblockUser(@PathVariable Long id) {
        try {
            userService.unlockUserById(id);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.ok(USER_UNLOCK);
    }

    @PutMapping("/change-role/{userId}")
    public ResponseEntity<String> changeUserRole(@PathVariable Long userId, @RequestBody RoleDTO roleDTO) {
        if (userService.checkPermissions(roleDTO.getAdminId(), roleDTO.getAdminPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(FORBIDDEN_OPERATION);
        }
        try {
            userService.changeUserRole(userId, roleDTO.getNewRole());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return ResponseEntity.ok(ROLE_CHANGED);
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable Long userId, @RequestBody RoleDTO roleDTO) {
        if (userService.checkPermissions(roleDTO.getAdminId(), roleDTO.getAdminPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(FORBIDDEN_OPERATION);
        }
        try {
            userService.deleteUserAccountById(userId);
        } catch (Exception ex) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        return ResponseEntity.ok(USER_DELETED);
    }

    private UserDTO convertToUserDto(User user) {
        return UserDTO
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .enabled(user.isEnabled())
                .accountNonLocked(user.isAccountNonLocked())
                .registrationDate(user.getRegistrationDate())
                .build();
    }
}
