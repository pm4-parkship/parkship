package ch.zhaw.backend;

import ch.zhaw.backend.todos.Todo;
import ch.zhaw.backend.todos.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Seed implements CommandLineRunner {
    private final TodoRepository todoRepository;

    @Autowired
    public Seed(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    private void seedDatabase() {
        //todoRepository.deleteAll();
        long count = todoRepository.count();
        if(count == 0) {
            List<Todo> todos = Arrays.asList(
                    new Todo("Learn spring boot", "Java sucks!"),
                    new Todo("Learn react.js", "next.js rocks!"),
                    new Todo("Learn docker", "containerize this!"),
                    new Todo("Learn typescript", "Also google monad"),
                    new Todo("Switch to c# for backend", "...or F#"),
                    new Todo("Cuddle your cats", "They deserve it")
            );
            todoRepository.saveAll(todos);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        seedDatabase();
    }
}
