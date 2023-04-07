package ch.zhaw.parkship.examples.todos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

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
    /*
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public Todo readById(@PathVariable int id) {
        return todoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }*/

    @GetMapping("{title}")
    public Todo readTodoByTitle(@PathVariable String title) {
        return todoRepository.findTodoByTitleContaining(title).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
