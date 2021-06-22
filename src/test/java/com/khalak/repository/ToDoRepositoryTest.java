package com.khalak.repository;

import com.khalak.model.ToDo;
import com.khalak.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ToDoRepositoryTest {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldCreateNewToDo() {
        ToDo testToDo = new ToDo();
        testToDo.setTitle("Test");
        testToDo.setCreatedAt(LocalDateTime.now());
        ToDo expected = toDoRepository.save(testToDo);
        ToDo actual = toDoRepository.findById(expected.getId()).orElseGet(null);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDeleteToDo() {
        int expectedNumbersOfTodos = 6;
        ToDo toDotoDelete = toDoRepository.findById(13L).get();
        toDoRepository.delete(toDotoDelete);
        assertEquals(expectedNumbersOfTodos, toDoRepository.findAll().size());
    }

    @Test
    public void shouldFindAllToDos() {
        int expectedNumbersOfTodos = 7;
        assertEquals(expectedNumbersOfTodos, toDoRepository.findAll().size());
    }

    @Test
    public void shouldGetToDoByUserId() {
        ToDo toDo1 = new ToDo();
        toDo1.setTitle("title1");
        toDo1.setCreatedAt(LocalDateTime.now());

        ToDo toDo2 = new ToDo();
        toDo2.setTitle("title2");
        toDo2.setCreatedAt(LocalDateTime.now().minusDays(1));

        User owner = new User();
        owner.setFirstName("Firstname");
        owner.setLastName("Lastname");
        owner.setEmail("test@gmail.com");
        owner.setPassword("pass");

        toDo1.setOwner(owner);
        toDo2.setCollaborators(Arrays.asList(owner));

        userRepository.save(owner);
        toDoRepository.save(toDo1);
        toDoRepository.save(toDo2);

        List<ToDo> expected = Arrays.asList(toDo1, toDo2);
        List<ToDo> actual  = toDoRepository.getByUserId(owner.getId());
        assertEquals(expected.size(), actual.size());
    }
}
