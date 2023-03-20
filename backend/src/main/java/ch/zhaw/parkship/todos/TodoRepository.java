package ch.zhaw.parkship.todos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface TodoRepository extends JpaRepository<Todo, Integer> {


     // Example JPA named query
     Optional<Todo> findTodoByTitle(String title);


     // Example JPQL query
     @Query("select t from Todo t where t.title = ?1")
     Optional<Todo> findTodoByTitleJpgl(String title);

     @Query(value = "SELECT * FROM todo t WHERE t.title = ?1", nativeQuery = true)
     List<Todo> findTodoByTitleNative(String title);


}
