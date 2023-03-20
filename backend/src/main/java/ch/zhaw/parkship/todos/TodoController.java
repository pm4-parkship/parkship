package ch.zhaw.parkship.todos;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoRepository todoRepository;
    private final SpecialTodoService specialTodoService;

    @Autowired
    public TodoController(TodoRepository todoRepository, SpecialTodoService specialTodoService) {
        this.todoRepository = todoRepository;
        this.specialTodoService = specialTodoService;
    }

    @GetMapping
    public List<Todo> read() {
        return todoRepository.findAll();
    }


    @GetMapping("{title}")
    public Todo readTodoByTitle(@PathVariable String title) {
        return todoRepository.findTodoByTitle(title).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PutMapping("{id}")
    public Todo upsert(@PathVariable Integer id, @Valid @RequestBody Todo todo) {
        try {
            return todoRepository.save(todo);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("{id}")
    @Transactional
    public void delete(@PathVariable Integer id) {
        specialTodoService.verySpecial(id);
    }
}
