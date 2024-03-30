package kwgh0st.project.todoappbackend.model.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String passwordResetToken;
    private boolean enabled;
    private boolean accountNonLocked;
    private String role;
    private LocalDate registrationDate;
    private boolean wantTodosNotification;

}
