package com.patriciomascialino.minesweeper.model;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Document(collection = "users")
@Getter
public class User {
    @Id
    private ObjectId id;
    private final String name;
    private final Instant createdAt;

    @PersistenceConstructor
    protected User(String name, Instant createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    public User(String name) {
        this.name = name;
        this.createdAt = ZonedDateTime.now(ZoneOffset.UTC).toInstant();
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt.atZone(ZoneOffset.UTC);
    }
}
