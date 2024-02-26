package kwgh0st.project.todoappbackend.exception;

public class InvalidResetPasswordTokenException extends RuntimeException {
    public static final String MESSAGE = "Invalid reset password token!";
    public InvalidResetPasswordTokenException(String message) {
        super(message);
    }
}
