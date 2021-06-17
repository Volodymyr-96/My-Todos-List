package com.khalak.repository;

import com.khalak.model.Role;
import com.khalak.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @Transactional
    public void createUserTest(){
        User user = new User();
        user.setFirstName("Mike");
        user.setLastName("Brown");
        user.setEmail("m@mail.com");
        user.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");

        userRepository.save(user);
        User actual = userRepository.findByEmail("m@mail.com");

        Assertions.assertEquals("Mike", actual.getFirstName());
    }

    @Test
    public void readByIdTest(){
        User expected = new User();
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        expected.setRole(role);
        expected.setId(4L);
        expected.setFirstName("Mike");
        expected.setLastName("Brown");
        expected.setEmail("mike@mail.com");
        expected.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");

        User actual = userRepository.findById(4L).get();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void updateUserTest(){
        User expected = new User();
        Role role = new Role();
        role.setName("ADMIN");
        expected.setRole(role);
        expected.setId(4L);
        expected.setFirstName("Milko");
        expected.setLastName("Brown");
        expected.setEmail("milko@mail.com");
        expected.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");

        User actual = userRepository.findById(4L).get();
        actual.setFirstName("Milko");
        actual.setEmail("milko@mail.com");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void deleteUserTest(){
        int expected = userRepository.findAll().size() - 1;

        userRepository.deleteById(4L);
        int actual = userRepository.findAll().size();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Transactional
    public void getAllTest(){
        int expexted = 0;

        userRepository.deleteAll();
        List<User> actual = userRepository.findAll();

        Assertions.assertEquals(expexted, actual.size());
    }

    @Test
    public void findUserByEmailTest(){
        User expected = new User();
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        expected.setRole(role);
        expected.setId(4L);
        expected.setFirstName("Mike");
        expected.setLastName("Brown");
        expected.setEmail("mike@mail.com");
        expected.setPassword("$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");

        User actual = userRepository.findByEmail("mike@mail.com");

        Assertions.assertEquals(expected, actual);
    }
}
