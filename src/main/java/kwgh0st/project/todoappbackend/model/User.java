package kwgh0st.project.todoappbackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private boolean accountNonLocked = true;
    private LocalDate registrationDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(targetEntity = VerificationToken.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private VerificationToken verificationToken;
    @OneToOne(targetEntity = PasswordResetToken.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private PasswordResetToken passwordResetToken;
    @OneToOne(targetEntity = UpdatePropertiesToken.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private UpdatePropertiesToken updatePropertiesToken;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


}
