package kwgh0st.project.todoappbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import kwgh0st.project.todoappbackend.exception.EmailAlreadyExistException;
import kwgh0st.project.todoappbackend.exception.EmailNotFoundException;
import kwgh0st.project.todoappbackend.exception.InvalidRegistrationTokenException;
import kwgh0st.project.todoappbackend.exception.UserAlreadyExistException;
import kwgh0st.project.todoappbackend.model.dto.UserDTO;
import kwgh0st.project.todoappbackend.model.User;
import kwgh0st.project.todoappbackend.service.MailService;
import kwgh0st.project.todoappbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(value = "/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final MailService mailService;
    private final Environment env;
    private static final String REGISTRATION_SUCCESSFUL = "Registration successful! A message with confirmation link has been sent to your email. You will be automatically redirected to the login page!";
    private static final String NEW_TOKEN_SEND = "The verification link has been resent. Please check your email. You will be automatically redirected to the login page!";
    private static final String ACCOUNT_ALREADY_CONFIRMED = "Your account is already confirmed!";

    @PostMapping
    public ResponseEntity<String> registerNewUser(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        try {
            final User registered = userService.registerNewUserAccount(userDTO);
            userService.createVerificationTokenForUser(registered);
            mailService.sendConfirmRegistrationEmail(request, registered);
            return ResponseEntity.ok(REGISTRATION_SUCCESSFUL);
        } catch (UserAlreadyExistException uaeEx) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(UserAlreadyExistException.MESSAGE);
        } catch (EmailAlreadyExistException eaeEx) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EmailAlreadyExistException.MESSAGE);
        }
    }

    @GetMapping("/confirm")
    public ModelAndView confirmRegistration(@RequestParam("token") String token) {
        try {
            userService.validateVerificationToken(token);
        } catch (InvalidRegistrationTokenException ex) {
            return new ModelAndView("redirect:" + env.getProperty("spring.client.url") + "/verification-error");
        }

        ModelAndView modelAndView = new ModelAndView(new RedirectView(env.getProperty("spring.client.url") + "/login"));
        modelAndView.addObject("confirmationSuccess", true);

        return modelAndView;
    }


    @PostMapping("/resend-registration-token")
    public ResponseEntity<String> resendRegistrationToken(
            HttpServletRequest request, @RequestParam("email") String email) {

        final User user = userService.getUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EmailNotFoundException.MESSAGE);
        }
        if (user.getVerificationToken() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ACCOUNT_ALREADY_CONFIRMED);
        }


        userService.generateNewVerificationToken(user.getVerificationToken().getToken());
        mailService.resendVerificationTokenEmail(request, user);

        return ResponseEntity.ok(NEW_TOKEN_SEND);
    }

}
