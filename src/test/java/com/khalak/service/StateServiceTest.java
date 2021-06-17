package com.khalak.service;


import com.khalak.model.State;
import com.khalak.model.Task;
import com.khalak.repository.StateRepository;
import com.khalak.service.impl.StateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StateServiceTest {

    @Mock
    StateRepository stateRepository;

    @InjectMocks
    StateServiceImpl stateService;

    State expected;
    final Long STATE_ID = 1L;
    final String NAME = "name";

    @BeforeEach
    void setUp() {
        expected = new State();

        List<Task> taskList = Arrays.asList(new Task(), new Task());

        expected.setName(NAME);
        expected.setTasks(taskList);
    }

    @Test
    public void createStateAndReturnTest() {
        when(stateRepository.save(any(State.class))).thenReturn(expected);

        State actual = stateService.create(expected);

        Assertions.assertEquals(expected, actual);
        verify(stateRepository).save(any(State.class));
    }

    @Test
    public void readByIdTest() {
        when(stateRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        State actual = stateService.readById(STATE_ID);

        Assertions.assertEquals(expected, actual);
        verify(stateRepository).findById(anyLong());
    }

    @Test
    public void updateStateTest() {
        when(stateRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(stateRepository.save(any(State.class))).thenReturn(expected);

        State actual = stateService.update(expected);

        Assertions.assertEquals(expected, actual);
        verify(stateRepository).findById(anyLong());
        verify(stateRepository).save(any(State.class));
    }

    @Test
    public void deleteStateTest () {
        when(stateRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        stateService.delete(STATE_ID);

        verify(stateRepository).findById(anyLong());
        verify(stateRepository).delete(any(State.class));
    }

    @Test
    public void getByNameTest() {
        when(stateRepository.findByName(anyString())).thenReturn(expected);

        State actual = stateService.getByName("name");

        verify(stateRepository).findByName(anyString());
        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    public void returnNewArrayListTest() {
        when(stateRepository.getAll()).thenReturn(Collections.emptyList());

        List<State> actual = stateService.getAll();

        Assertions.assertEquals(0, actual.size());
        verify(stateRepository).getAll();
    }

    @Test
    public void returnStateListTest() {
        List<State> expected = Arrays.asList(new State(), new State());

        when(stateRepository.getAll()).thenReturn(expected);

        List<State> actual = stateService.getAll();

        verify(stateRepository).getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void entityNotFoundExceptionGetByNameTest() {
        when(stateRepository.findByName(anyString())).thenReturn(null);

        String name = "name";

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            stateService.getByName(name);
        });

        Assertions.assertEquals("State with name '" + name + "' not found", exception.getMessage());
        verify(stateRepository).findByName(anyString());
    }

    @Test
    public void throwEntityNotFoundExceptionStateTest() {
        when(stateRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            stateService.delete(STATE_ID);
        });

        Assertions.assertEquals("State with id " + STATE_ID + " not found", exception.getMessage());
        verify(stateRepository).findById(anyLong());
        verify(stateRepository, times(0)).delete(any(State.class));
    }
}