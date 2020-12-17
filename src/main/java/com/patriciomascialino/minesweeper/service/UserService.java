package com.patriciomascialino.minesweeper.service;

import com.patriciomascialino.minesweeper.exception.InvalidUserIdException;
import com.patriciomascialino.minesweeper.exception.UserNotFoundException;
import com.patriciomascialino.minesweeper.model.User;
import com.patriciomascialino.minesweeper.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User newUser(final String name) {
        User user = new User(name);
        userRepository.save(user);
        return user;
    }

    public User getUser(final String userId) {
        if (!ObjectId.isValid(userId))
            throw new InvalidUserIdException(userId);
        return userRepository.findById(new ObjectId(userId))
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
