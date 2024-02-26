package kwgh0st.project.todoappbackend.exception;

public class ResourceNotFoundException extends RuntimeException {
    public static final String MESSAGE = "Resource not found!";

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
