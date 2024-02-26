package kwgh0st.project.todoappbackend.controller;

import kwgh0st.project.todoappbackend.security.jwt.JwtTokenRequest;
import kwgh0st.project.todoappbackend.security.jwt.JwtTokenResponse;
import kwgh0st.project.todoappbackend.security.jwt.JwtTokenService;
import kwgh0st.project.todoappbackend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> generateToken(
            @RequestBody JwtTokenRequest jwtTokenRequest) {

        return authenticationService.authenticate(jwtTokenRequest);
    }
}
