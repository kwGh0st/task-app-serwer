package kwgh0st.project.todoappbackend.service;

import jakarta.transaction.Transactional;
import kwgh0st.project.todoappbackend.exception.UserNotFoundException;
import kwgh0st.project.todoappbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> (new UsernameNotFoundException(UserNotFoundException.MESSAGE)));
    }

}
