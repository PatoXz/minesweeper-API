package com.patriciomascialino.minesweeper.repository;

import com.patriciomascialino.minesweeper.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
}
