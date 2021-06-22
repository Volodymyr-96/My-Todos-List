package com.khalak.service;

import com.khalak.exception.NullEntityReferenceException;
import com.khalak.model.Priority;
import com.khalak.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@SpringBootTest
public class TaskServiceTest {

    @Autowired
    TaskService taskService;

    @Test
    @Transactional
    public void testCreateValidTask() {
        Task task = new Task();
        task.setName("Create test");
        long beforeCreate = taskService.getAll().size()+1;

        Assertions.assertEquals(task, taskService.create(task));
        Assertions.assertEquals(beforeCreate, taskService.getAll().size());
    }

    @Test
    @Transactional
    public void testValidReadById() {
        Task task = new Task();
        task.setName("Task #1");
        task.setPriority(Priority.HIGH);

        taskService.create(task);
        Task expected = taskService.readById(task.getId());

        Assertions.assertEquals(expected, task);
    }

    @Test
    public void testInvalidReadById() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.readById(357), "Task with id " + 357 + " not found");
    }

    @Test
    @Transactional
    public void testDelete() {
        Task task = new Task();
        task.setName("Delete test");

        taskService.create(task);
        taskService.delete(task.getId());

        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.readById(task.getId()), "Task with selected id not found");
    }

    @Test
    @Transactional
    public void testUpdate() {
        Task task = taskService.readById(7L);
        task.setName("updated");

        taskService.update(task);

        Assertions.assertEquals("updated", taskService.readById(7L).getName());
    }

    @Test
    public void testInvalidUpdate() {
        Assertions.assertThrows(NullEntityReferenceException.class, () -> taskService.update(null), "Updated task cannot be null");
        Task task = new Task();
        task.setId(911L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.update(task), "No task to update");
    }

    @Test
    public void testFindAll() {
        Assertions.assertEquals(3, taskService.getAll().size());
    }

    @Test
    public void testGetByTodoId() {
        Assertions.assertEquals(3, taskService.getByTodoId(7L).size());
    }
}