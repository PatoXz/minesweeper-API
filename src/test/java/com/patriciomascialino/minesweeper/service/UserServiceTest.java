package com.patriciomascialino.minesweeper.service;

import com.patriciomascialino.minesweeper.exception.InvalidUserIdException;
import com.patriciomascialino.minesweeper.exception.UserNotFoundException;
import com.patriciomascialino.minesweeper.model.User;
import com.patriciomascialino.minesweeper.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    public void beforeEach() {
        this.userRepository = mock(UserRepository.class);
        this.userService = new UserService(userRepository);
    }

    @Test
    public void newUserTest() {
        when(userRepository.save(any())).thenReturn(new User("test name"));
        final User user = userService.newUser("test name");
        assertEquals("test name", user.getName());
        assertNotNull(user.getCreatedAt());
        assertNull(user.getId());
    }

    @Test
    public void getUserTest() {
        ObjectId userId = new ObjectId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User("test name")));
        final User user = userService.getUser(userId.toString());
        assertEquals("test name", user.getName());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    public void tryToGetNonExistentUserThrowsExceptionTest() {
        final ObjectId userId = new ObjectId();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUser(userId.toString()));
    }

    @Test
    public void throwExceptionWhenInvalidUserId() {
        final InvalidUserIdException exception = assertThrows(InvalidUserIdException.class,
                () -> userService.getUser("invalidUserId"));
        assertEquals("Invalid user id format. Received: invalidUserId", exception.getMessage());
    }
}
