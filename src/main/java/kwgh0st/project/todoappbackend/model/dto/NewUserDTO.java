package kwgh0st.project.todoappbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserDTO {
    private Long adminId;
    private String adminPassword;
    private String email;
    private String role;
}
