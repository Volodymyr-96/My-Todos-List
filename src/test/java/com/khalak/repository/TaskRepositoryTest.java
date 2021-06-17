package com.khalak.repository;

import com.khalak.model.Priority;
import com.khalak.model.Task;
import com.khalak.model.ToDo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ToDoRepository toDoRepository;

    @Test
    @Transactional
    public void createTaskTest() {
        Task task = new Task();
        task.setName("Task #1");
        task.setPriority(Priority.HIGH);

        taskRepository.save(task);

        Assertions.assertEquals("Task #1", taskRepository.getOne(task.getId()).getName());
    }

    @Test
    @Transactional
    public void readByIdTest() {
        Task task = new Task();
        task.setName("Task #1");
        task.setPriority(Priority.HIGH);
        taskRepository.save(task);

        Task expected = taskRepository.findById(task.getId()).get();

        Assertions.assertEquals(expected, task);
    }

    @Test
    @Transactional
    public void updateTaskTest() {
        Task task = new Task();
        task.setName("Task #1");
        task.setPriority(Priority.HIGH);
        taskRepository.save(task);

        Task actual = taskRepository.findById(task.getId()).get();
        actual.setName("Task #911");
        taskRepository.save(actual);

        Assertions.assertEquals("Task #911", taskRepository.getOne(actual.getId()).getName());
    }

    @Test
    @Transactional
    public void deleteTaskTest() {
        int expected = taskRepository.findAll().size() - 1;

        taskRepository.deleteById(7L);

        Assertions.assertEquals(expected, taskRepository.findAll().size());
    }

    @Test
    @Transactional
    public void getByTodoIdTest() {
        ToDo todo = new ToDo();
        todo.setTitle("Tasks todo");
        todo.setCreatedAt(LocalDateTime.now());
        toDoRepository.save(todo);

        Task task = new Task();
        task.setName("Task #1");
        task.setPriority(Priority.HIGH);
        task.setTodo(todo);
        taskRepository.save(task);

        List<Task> expected = new ArrayList<>();
        expected.add(task);

        Assertions.assertEquals(expected, taskRepository.getByTodoId(todo.getId()));
    }


}
