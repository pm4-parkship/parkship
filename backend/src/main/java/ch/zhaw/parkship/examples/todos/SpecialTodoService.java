package ch.zhaw.parkship.examples.todos;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

@Component
public class SpecialTodoService {

    private final TodoRepository todoRepository;

    public SpecialTodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public void verySpecial(Integer id) {
        try {
            todoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item could not be deleted");
        }
    }

}
