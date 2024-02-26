package kwgh0st.project.todoappbackend.repository;

import kwgh0st.project.todoappbackend.model.UpdatePropertiesToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UpdatePropertiesTokenRepository extends JpaRepository<UpdatePropertiesToken, Long> {
    Optional<UpdatePropertiesToken> findByToken(String token);
}
