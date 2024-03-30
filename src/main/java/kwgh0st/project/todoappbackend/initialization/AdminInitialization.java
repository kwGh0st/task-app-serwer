package kwgh0st.project.todoappbackend.initialization;

import kwgh0st.project.todoappbackend.model.User;
import kwgh0st.project.todoappbackend.model.dto.UserDTO;
import kwgh0st.project.todoappbackend.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Setter
@Getter
@RequiredArgsConstructor
@Configuration
public class AdminInitialization implements ApplicationRunner {
    private final UserService userService;
    private final Environment env;

    @Override
    public void run(ApplicationArguments args) {

        User adminUser = userService.getUserByUsername(env.getProperty("spring.init.admin.username"));

        if (adminUser == null) {
            try {
                userService.registerNewAdminAccount(
                        UserDTO
                                .builder()
                                .username(env.getProperty("spring.init.admin.username"))
                                .password(env.getProperty("spring.init.admin.password"))
                                .email(env.getProperty("spring.init.admin.email"))
                                .enabled(true)
                                .accountNonLocked(true)
                                .wantTodosNotification(true)
                                .build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
