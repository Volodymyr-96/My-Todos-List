package com.khalak.service;

import com.khalak.exception.NullEntityReferenceException;
import com.khalak.model.ToDo;
import com.khalak.model.User;
import com.khalak.repository.ToDoRepository;
import com.khalak.service.impl.ToDoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ToDoServiceTest {

    @Mock
    ToDoRepository toDoRepository;

    @InjectMocks
    private ToDoServiceImpl toDoService;

    ToDo expected;
    final Long TODO_ID = 1L;
    final Long USER_ID = 2L;
    final String TITLE = "title";

    @BeforeEach
    public void setUp() {
        User owner = new User();
        owner.setId(USER_ID);

        expected = new ToDo();
        expected.setId(TODO_ID);
        expected.setTitle(TITLE);
        expected.setOwner(owner);
    }

    @Test
    @Transactional
    public void shouldCreateToDo() {
        when(toDoRepository.save(any())).thenReturn(expected);
        ToDo actual = toDoService.create(expected);
        verify(toDoRepository).save(any(ToDo.class));
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    @Transactional
    public void shouldReadById() {
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        ToDo actual = toDoService.readById(TODO_ID);
        verify(toDoRepository).findById(anyLong());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionReadById() {
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> toDoService.readById(TODO_ID));
    }

    @Test
    @Transactional
    public void shouldUpdateToDo() {
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(toDoRepository.save(any(ToDo.class))).thenReturn(expected);
        ToDo actual = toDoService.update(expected);
        verify(toDoRepository).save(any(ToDo.class));
        verify(toDoRepository).findById(anyLong());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    @Transactional
    void shouldThrowNullEntityReferenceExceptionUpdateToDo() {
        assertThrows(NullEntityReferenceException.class, () -> toDoService.update(null));
    }

    @Test
    @Transactional
    void shouldDeleteToDo() {
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        toDoService.delete(TODO_ID);
        verify(toDoRepository).findById(anyLong());
        verify(toDoRepository).delete(any(ToDo.class));
    }

    @Test
    @Transactional
    void shouldThrowEntityNotFoundExceptionDeleteToDo() {
        when(toDoRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> toDoService.delete(TODO_ID));
        verify(toDoRepository).findById(anyLong());
        verify(toDoRepository, times(0)).delete(any(ToDo.class));
    }

    @Test
    public void shouldReturnListAllTodosTest() {
        List<ToDo> expected = Arrays.asList(new ToDo(), new ToDo());
        when(toDoRepository.findAll()).thenReturn(expected);
        List<ToDo> actual = toDoService.getAll();
        Assertions.assertEquals(expected.size(), actual.size());
        verify(toDoRepository).findAll();
    }

    @Test
    public void shouldReturnListToDoGetByUserId() {
        List<ToDo> expected = Arrays.asList(new ToDo(), new ToDo());
        when(toDoRepository.getByUserId(anyLong())).thenReturn(expected);
        List<ToDo> actual = toDoService.getByUserId(USER_ID);
        verify(toDoRepository).getByUserId(anyLong());
        Assertions.assertEquals(expected, actual);
    }
}