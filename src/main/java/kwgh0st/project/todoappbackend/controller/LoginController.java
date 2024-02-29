package kwgh0st.project.todoappbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import kwgh0st.project.todoappbackend.exception.EmailNotFoundException;
import kwgh0st.project.todoappbackend.exception.InvalidResetPasswordTokenException;
import kwgh0st.project.todoappbackend.exception.InvalidUpdatePropertiesTokenException;
import kwgh0st.project.todoappbackend.exception.UserNotFoundException;
import kwgh0st.project.todoappbackend.model.User;
import kwgh0st.project.todoappbackend.model.dto.PasswordDTO;
import kwgh0st.project.todoappbackend.model.dto.UserDTO;
import kwgh0st.project.todoappbackend.service.MailService;
import kwgh0st.project.todoappbackend.service.PasswordTokenService;
import kwgh0st.project.todoappbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final PasswordTokenService passwordTokenService;
    private final MailService mailService;
    private final Environment env;

    private static final String SEND_RESET_PASSWORD_TOKEN = "An email with instructions for password change has been sent. You will be automatically redirected to the login page!";
    private static final String PASSWORD_UPDATED = "Password successfully updated!. You will be automatically redirected to the login page!";
    private static final String CREDENTIALS_UPDATED = "Your credentials has been successfully updated. Now you can log in.";

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(HttpServletRequest request,
                                                @RequestParam("email") String userEmail) {

        User user = userService.getUserByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EmailNotFoundException.MESSAGE);
        }

        userService.createPasswordResetTokenForUser(user);
        mailService.sendResetPasswordTokenMessage(request, user);

        return ResponseEntity.ok(SEND_RESET_PASSWORD_TOKEN);
    }

    @GetMapping("/validate-token")
    public ModelAndView validatePasswordResetToken(@RequestParam("token") String token) {
        String result = passwordTokenService.validatePasswordResetToken(token);
        if (result != null) {
            throw new InvalidResetPasswordTokenException(InvalidResetPasswordTokenException.MESSAGE);
        }

        String frontendRedirectUrl = env.getProperty("spring.client.url") + "/user/login/reset-password";

        ModelAndView modelAndView = new ModelAndView(new RedirectView(frontendRedirectUrl));
        modelAndView.addObject("passwordToken", token);

        return modelAndView;
    }


    @PostMapping("/save-new-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordDTO passwordDto) {
        String result = passwordTokenService.validatePasswordResetToken(passwordDto.getPasswordToken());
        if (result != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(InvalidResetPasswordTokenException.MESSAGE);
        }

        User user = userService.getUserByPasswordResetToken(passwordDto.getPasswordToken());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserNotFoundException.MESSAGE);
        } else {
            userService.changeUserPassword(user, passwordDto.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(PASSWORD_UPDATED);
        }
    }

    @GetMapping("/validate-update-credentials-token")
    public ModelAndView validateUpdatePropertiesToken(@RequestParam("token") String token) {
        if (userService.isValidUpdatePropertiesToken(token))
            throw new InvalidUpdatePropertiesTokenException(InvalidUpdatePropertiesTokenException.MESSAGE);

        String frontendRedirectUrl = env.getProperty("spring.client.url") + "/user/login/update-credentials";
        ModelAndView modelAndView = new ModelAndView(new RedirectView(frontendRedirectUrl));
        modelAndView.addObject("updatePropertiesToken", token);

        return modelAndView;
    }

    @PostMapping("/update-credentials-by-token/{updateCredentialsToken}")
    public ResponseEntity<String> updateUserCredentialsByToken(@PathVariable String updateCredentialsToken, @RequestBody UserDTO userDTO) {
        if (userService.isValidUpdatePropertiesToken(updateCredentialsToken))
            throw new InvalidUpdatePropertiesTokenException(InvalidUpdatePropertiesTokenException.MESSAGE);

        userService.updateUserPropertiesWithToken(updateCredentialsToken, userDTO);

        return ResponseEntity.ok(CREDENTIALS_UPDATED);
    }
}


