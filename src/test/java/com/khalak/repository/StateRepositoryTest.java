package com.khalak.repository;

import com.khalak.model.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StateRepositoryTest {

    @Autowired
    StateRepository stateRepository;

    @Autowired
    private TestEntityManager manager;

    @Test
    void returnStateByName() {
        String name = "testing";
        State expected = new State();
        expected.setName(name);

        stateRepository.save(expected);

        State actual = stateRepository.findByName(name);

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void returnListOfState() {
        String name = "testing";
        State expected = new State();
        expected.setName(name);

        stateRepository.save(expected);

        List<State> actual = stateRepository.getAll();

        Assertions.assertNotEquals(0, actual.size());
    }

    @Test
    void returnNull() {
        String name = "testing2";
        State actual = stateRepository.findByName(name);

        Assertions.assertNull(actual);
    }
}
