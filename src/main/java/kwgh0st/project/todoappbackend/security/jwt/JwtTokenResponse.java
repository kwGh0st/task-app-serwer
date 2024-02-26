package kwgh0st.project.todoappbackend.security.jwt;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
public record JwtTokenResponse(String token, String roles) {

}
