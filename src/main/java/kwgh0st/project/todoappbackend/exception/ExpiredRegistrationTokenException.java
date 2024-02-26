package kwgh0st.project.todoappbackend.exception;

public class ExpiredRegistrationTokenException extends RuntimeException {
    public static final String MESSAGE = "Token expired!";
    public ExpiredRegistrationTokenException(String message) {
        super(message);
    }
}
