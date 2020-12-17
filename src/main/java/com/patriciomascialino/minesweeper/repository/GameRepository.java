package com.patriciomascialino.minesweeper.repository;

import com.patriciomascialino.minesweeper.model.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GameRepository extends MongoRepository<Game, ObjectId> {
    Optional<Game> findByIdAndUserID(ObjectId gameId, ObjectId userId);
}
