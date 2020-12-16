package com.patriciomascialino.minesweeper.repository;

import com.patriciomascialino.minesweeper.model.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
}
