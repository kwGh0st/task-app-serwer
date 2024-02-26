package kwgh0st.project.todoappbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String newPassword;
    private String passwordToken;


}
