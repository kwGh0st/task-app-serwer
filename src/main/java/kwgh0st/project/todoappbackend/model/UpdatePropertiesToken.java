package kwgh0st.project.todoappbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class UpdatePropertiesToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private boolean expired;

    public UpdatePropertiesToken() {
    }

    public UpdatePropertiesToken(String token) {
        this.token = token;
        this.expired = false;
    }

    @Override
    public String toString() {
        return token;
    }
}
