package kwgh0st.project.todoappbackend.controller;

import kwgh0st.project.todoappbackend.model.Todo;
import kwgh0st.project.todoappbackend.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;


    @GetMapping("/{username}/todos")
    public List<Todo> getAllTodos(@PathVariable String username) {
        return todoService.getTodoByUsername(username);
    }


    @GetMapping("/{username}/todos/{id}")
    public Todo getTodo(@PathVariable Long id) {
        return todoService.getTodoById(id);
    }


    @DeleteMapping("/{username}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{username}/todos/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        todoService.updateTodoById(id, todoDetails);
        return todoDetails;
    }


    @PostMapping("/{username}/todos")
    public Todo createTodo(@RequestBody Todo todo)  {

        return todoService.addNewTodo(todo);
    }
}
