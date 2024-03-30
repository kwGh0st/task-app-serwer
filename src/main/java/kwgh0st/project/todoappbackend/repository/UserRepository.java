package kwgh0st.project.todoappbackend.repository;

import kwgh0st.project.todoappbackend.model.PasswordResetToken;
import kwgh0st.project.todoappbackend.model.UpdatePropertiesToken;
import kwgh0st.project.todoappbackend.model.User;
import kwgh0st.project.todoappbackend.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByVerificationToken(VerificationToken verificationToken);
    Optional<User> findByPasswordResetToken(PasswordResetToken passwordResetToken);

    Optional<User> findByUpdatePropertiesToken(UpdatePropertiesToken updatePropertiesToken);
    Optional<User> existsByUpdatePropertiesToken(UpdatePropertiesToken updatePropertiesToken);

    Optional<List<User>> findByWantTodosNotificationTrue();
}
