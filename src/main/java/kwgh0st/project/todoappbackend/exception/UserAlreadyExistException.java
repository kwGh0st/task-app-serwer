package kwgh0st.project.todoappbackend.exception;

public class UserAlreadyExistException extends RuntimeException {
    public static final String MESSAGE = "Username already exist!";

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
