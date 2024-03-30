package kwgh0st.project.todoappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class TodoAppBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoAppBackendApplication.class, args);
    }

}
