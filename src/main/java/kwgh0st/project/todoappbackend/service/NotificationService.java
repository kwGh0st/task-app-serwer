package kwgh0st.project.todoappbackend.service;

import kwgh0st.project.todoappbackend.model.Todo;
import kwgh0st.project.todoappbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final TodoService todoService;
    private final MailService mailService;
    private final UserService userService;


    @Autowired
    public NotificationService(TodoService todoService, MailService mailService, UserService userService) {
        this.todoService = todoService;
        this.mailService = mailService;
        this.userService = userService;
    }

    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void checkAndSendNotifications() {
        List<User> users = userService.getUserByWantNotification();
        System.out.println(users);

        for (User user : users) {
            List<Todo> todosForToday = todoService.getTodosForToday(user.getUsername(), LocalDate.now());

            if (!todosForToday.isEmpty()) {
                sendTodoNotificationEmail(user, todosForToday);
            }
        }
    }

    private void sendTodoNotificationEmail(User user, List<Todo> todosForToday) {
        mailService.sendNotificationEmail(user, todosForToday);
    }
}
