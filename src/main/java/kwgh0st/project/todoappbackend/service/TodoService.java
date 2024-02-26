package kwgh0st.project.todoappbackend.service;

import jakarta.transaction.Transactional;
import kwgh0st.project.todoappbackend.exception.ResourceNotFoundException;
import kwgh0st.project.todoappbackend.model.Todo;
import kwgh0st.project.todoappbackend.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Todo getTodoById(Long id) {
        if (id == -1) return null;

        return todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundException.MESSAGE));
    }

    public void deleteTodoById(Long id) {
        Todo todo = getTodoById(id);
        todoRepository.delete(todo);
    }

    public List<Todo> getTodoByUsername(String username) {
        return todoRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundException.MESSAGE));
    }

    public void updateTodoById(Long id, Todo todoDetails) {

        Todo todo = getTodoById(id);
        todo.setTargetDate(todoDetails.getTargetDate());
        todo.setDescription(todoDetails.getDescription());
        todo.setDone(todoDetails.isDone());
        todo.setUsername(todoDetails.getUsername());

        todoRepository.save(todo);
    }

    public Todo addNewTodo(Todo todo) {
        return todoRepository.save(todo);
    }

}
