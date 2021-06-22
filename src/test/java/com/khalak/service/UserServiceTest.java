package com.khalak.service;

import com.khalak.exception.NullEntityReferenceException;
import com.khalak.model.User;
import com.khalak.repository.UserRepository;
import com.khalak.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    private List<User> users;

    @BeforeEach
    public void mockDataSetUp() {

        User user1 = new User();
        user1.setId(6L);
        user1.setFirstName("Nora");
        user1.setLastName("White");
        user1.setEmail("nora@mail.com");
        user1.setPassword("$2a$10$yYQaJrHzjOgD5wWCyelp0e1Yv1KEKeqUlYfLZQ1OQvyUrnEcX/rOy");

        User user2 = new User();
        user2.setId(5L);
        user2.setFirstName("Nick");
        user2.setLastName("Green");
        user2.setEmail("nick@mail.com");
        user2.setPassword("$2a$10$CJgEoobU2gm0euD4ygru4ukBf9g8fYnPrMvYk.q0GMfOcIDtUhEwC");

        User user3 = new User();
        user3.setId(4L);
        user3.setFirstName("Mike");
        user3.setLastName("Brown");
        user3.setEmail("mike@mail.com");
        user3.setPassword("$$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO");

        users = Arrays.asList(user1,user2,user3);

        when(userRepository.findById(6L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(8L)).thenReturn(Optional.ofNullable(null));
    }

    @Test
    public void createUserTest() {
        when(userRepository.save(any(User.class))).thenReturn(users.get(0));
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Willson");
        user.setEmail("yyy@jjj.com");
        user.setPassword("jksjnhi4890309jkdjso");
        user.setId(7L);
        userService.create(user);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void readByIdUserTest() {
        String firstName = "Nora";
        assertEquals(userService.readById(6L).getFirstName(), firstName);
    }

    @Test
    public void updateUserTest() {
        String firstName = "Maria";
        User user = userService.readById(6L);
        user.setFirstName(firstName);
        userService.update(user);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUserNullTest() {
        assertThrows(NullEntityReferenceException.class, () -> userService.update(null));
    }


    @Test
    public void deleteUserTest() {
        userService.delete(6L);
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    public void deleteNullUserTest() {
        assertThrows(EntityNotFoundException.class, () -> userService.delete(8L));
    }

    @Test
    public void getAllUsersTest() {
        userService.getAll();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getAllUsersEmptyTest() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        List<User> users = userService.getAll();
        verify(userRepository, times(1)).findAll();
        assertEquals(0, users.size() );
    }
}