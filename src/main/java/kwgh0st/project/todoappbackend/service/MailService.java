package kwgh0st.project.todoappbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import kwgh0st.project.todoappbackend.model.User;
import kwgh0st.project.todoappbackend.model.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MessageSource messages;
    private final SimpleMailMessage email;

    public void ResendVerificationTokenEmail(final HttpServletRequest request, final User user) {
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), user.getVerificationToken(), user));
    }

    public void sendConfirmRegistrationEmail(final HttpServletRequest request, final User user) {
        mailSender.send(constructRegistrationEmailMessage(getAppUrl(request), user, user.getVerificationToken().getToken(), request.getLocale()));
    }

    public void sendResetPasswordTokenMessage(final HttpServletRequest request, final User user) {
        mailSender.send(constructResetPasswordEmailMessage(getAppUrl(request), user, user.getPasswordResetToken().getToken(), request.getLocale()));
    }

    public void sendSuccessEmailChangedMessage(final HttpServletRequest request, final String newEmail) {
        mailSender.send(constructEmailChangedMessage(newEmail, request.getLocale()));
    }

    public void sendSuccessPasswordChangeEmail(final HttpServletRequest request, final User user) {
        mailSender.send(constructPasswordChangedEmailMessage(request.getLocale(), user));
    }

    public void sendSuccessAccountDeletedMessage(final HttpServletRequest request, final User user) {
        mailSender.send(constructAccountDeletedMessage(request.getLocale(), user));
    }

    public void sendBlockedAccountMessage(final HttpServletRequest request, final User user) {
        mailSender.send(constructBlockedAccountMessage(request.getLocale(), user));
    }

    public void sendAccountCreatedByAdminMessage(final HttpServletRequest request, final User user) {
        mailSender.send(constructAccountCreatedByAdminMessage(getAppUrl(request), user.getUpdatePropertiesToken().getToken(), user, request.getLocale()));
    }

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String subject = "New registration token.";
        final String confirmationUrl = contextPath + "/register/confirm?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        final String body = message + " \r\n" + confirmationUrl;
        return constructEmail(subject, body, user.getEmail());
    }


    private SimpleMailMessage constructRegistrationEmailMessage(final String contextPath, final User user, final String token, final Locale locale) {
        final String subject = "Registration Confirmation";
        final String confirmationUrl = contextPath + "/register/confirm?token=" + token;
        final String message = messages.getMessage("message.regSuccess", null, locale);
        final String body = message + " \r\n" + confirmationUrl;
        return constructEmail(subject, body, user.getEmail());
    }

    private SimpleMailMessage constructResetPasswordEmailMessage(final String contextPath, final User user, final String passwordResetToken, final Locale locale) {
        final String subject = "Reset your password";
        final String confirmationUrl = contextPath + "/login/validate-token?token=" + passwordResetToken;
        final String message = messages.getMessage("message.resPassword", null, locale);
        final String body = message + " \r\n" + confirmationUrl;
        return constructEmail(subject, body, user.getEmail());
    }

    private SimpleMailMessage constructEmailChangedMessage(final String newEmail, final Locale locale) {
        final String subject = "Your email has been changed";
        final String message = messages.getMessage("message.emailChanged", null, locale);
        final String body = message + "\r\nNew email: " + newEmail;

        return constructEmail(subject, body, newEmail);
    }

    private SimpleMailMessage constructPasswordChangedEmailMessage(final Locale locale, User user) {
        final String subject = "Your password has been changed";
        final String body = messages.getMessage("message.passwordChanged", null, locale);
        return constructEmail(subject, body, user.getEmail());
    }

    private SimpleMailMessage constructAccountDeletedMessage(final Locale locale, User user) {
        final String subject = "Your account has been deleted.";
        final String body = messages.getMessage("message.accountDeleted", null, locale);
        return constructEmail(subject, body, user.getEmail());
    }

    private SimpleMailMessage constructBlockedAccountMessage(final Locale locale, User user) {
        final String subject = "Your account has been blocked.";
        final String body = messages.getMessage("message.userAccountBlocked", null, locale);
        return constructEmail(subject, body, user.getEmail());
    }

    private SimpleMailMessage constructAccountCreatedByAdminMessage(final String contextPath, final String updatePropertiesToken, final User user, final Locale locale) {
        final String subject = "We created an account for you!";
        final String confirmationUrl = contextPath + "/login/validate-update-credentials-token?token=" + updatePropertiesToken;
        final String message = messages.getMessage("message.accountCreatedByAdmin", null, locale);
        final String body = message + "\r\n" + confirmationUrl;
        return constructEmail(subject, body, user.getEmail());
    }

    private SimpleMailMessage constructEmail(String subject, String body, String toEmail) {
        email.setSubject(subject);
        email.setText(body);
        email.setTo(toEmail);
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
