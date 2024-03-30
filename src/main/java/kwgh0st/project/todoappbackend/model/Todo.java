package kwgh0st.project.todoappbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kwgh0st.project.todoappbackend.service.EncryptionService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Size(min = 5, max = 16)
    private String username;
    @NotNull
    private String description;
    @NotNull
    private LocalDate targetDate;
    private boolean done;

    public void setDescription(String description) {
        this.description = EncryptionService.encrypt(description);
    }

    public String getDescription() {
        return EncryptionService.decrypt(this.description);
    }

}
