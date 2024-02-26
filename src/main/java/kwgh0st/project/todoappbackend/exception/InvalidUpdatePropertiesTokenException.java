package kwgh0st.project.todoappbackend.exception;

public class InvalidUpdatePropertiesTokenException extends RuntimeException {
    public static final String MESSAGE = "Invalid update properties token!";
    public InvalidUpdatePropertiesTokenException(String message) {
        super(message);
    }
}
