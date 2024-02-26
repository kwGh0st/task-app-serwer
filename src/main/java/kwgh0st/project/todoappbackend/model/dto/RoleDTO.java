package kwgh0st.project.todoappbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {
    private String newRole;
    private String adminPassword;
    private Long adminId;

    @Override
    public String toString() {
        return "RoleDTO{" +
                "newRole='" + newRole + '\'' +
                ", adminPassword='" + adminPassword + '\'' +
                ", adminId=" + adminId +
                '}';
    }
}
