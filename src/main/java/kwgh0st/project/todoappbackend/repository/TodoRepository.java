package kwgh0st.project.todoappbackend.repository;

import java.util.List;
import java.util.Optional;

import kwgh0st.project.todoappbackend.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>{

   Optional<List<Todo>> findByUsername(String username);

}
