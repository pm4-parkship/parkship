package ch.zhaw.parkshhip;

import ch.zhaw.parkship.ParkshipApplication;
import ch.zhaw.parkship.todos.Todo;
import ch.zhaw.parkship.todos.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
public class ContextTest {

    @Autowired
    TodoRepository todoRepository;

    @Test
    public void hallo(){
        List<Todo> list = todoRepository.findAll();
        Optional<Todo> first = todoRepository.findTodoByTitle("Learn spring boot");
        Optional<Todo> second = todoRepository.findTodoByTitleJpgl("Learn spring boot");
        List<Todo> third = todoRepository.findTodoByTitleNative("Learn spring boot");
    }
}
