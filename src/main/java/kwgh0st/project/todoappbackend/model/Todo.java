package kwgh0st.project.todoappbackend.model;

import jakarta.persistence.*;
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
//    @Convert(converter = EncryptionService.class)
    @Size(min = 5)
    @Column(length = 1024)
    private String description;
    @NotNull
    private LocalDate targetDate;
    private boolean done;

}
