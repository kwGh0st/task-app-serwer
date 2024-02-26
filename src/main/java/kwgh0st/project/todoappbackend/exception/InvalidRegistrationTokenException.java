package kwgh0st.project.todoappbackend.exception;

public class InvalidRegistrationTokenException extends RuntimeException {
    public static final String MESSAGE = "Invalid registration token!";
    public InvalidRegistrationTokenException(String message) {
        super(message);
    }
}
