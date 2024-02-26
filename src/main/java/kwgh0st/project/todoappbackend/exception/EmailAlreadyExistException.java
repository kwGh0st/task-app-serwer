package kwgh0st.project.todoappbackend.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public static final String MESSAGE = "Email already exist!";
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
