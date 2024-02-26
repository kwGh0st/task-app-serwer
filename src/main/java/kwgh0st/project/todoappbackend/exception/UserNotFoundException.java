package kwgh0st.project.todoappbackend.exception;

public class UserNotFoundException extends RuntimeException {
    public static final String MESSAGE = "User not found!";
    public UserNotFoundException(String message) {
        super(message);
    }
}
