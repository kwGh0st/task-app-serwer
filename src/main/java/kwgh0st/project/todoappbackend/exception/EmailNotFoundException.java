package kwgh0st.project.todoappbackend.exception;

public class EmailNotFoundException extends RuntimeException {
    public static final String MESSAGE = "Email not found!";

    public EmailNotFoundException(String message) {
        super(message);
    }


}
