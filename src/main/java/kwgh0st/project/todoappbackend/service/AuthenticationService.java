package kwgh0st.project.todoappbackend.service;

import kwgh0st.project.todoappbackend.security.jwt.JwtTokenRequest;
import kwgh0st.project.todoappbackend.security.jwt.JwtTokenResponse;
import kwgh0st.project.todoappbackend.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationProvider authenticationProvider;
    private final JwtTokenService jwtTokenService;


    public ResponseEntity<JwtTokenResponse> authenticate(JwtTokenRequest jwtTokenRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                jwtTokenRequest.username(),
                jwtTokenRequest.password()
        );

        var authentication = authenticationProvider.authenticate(authenticationToken);

        var token = jwtTokenService.generateToken(authentication);


        return ResponseEntity.ok(new JwtTokenResponse(token, authentication.getAuthorities().toString()));
    }


}