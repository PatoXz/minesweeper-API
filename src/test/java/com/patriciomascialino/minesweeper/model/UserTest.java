package com.patriciomascialino.minesweeper.model;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    @Test
    public void persistenceConstructorTest() {
        User user = new User("name test", ZonedDateTime.parse("2020-12-16T00:00:00Z").toInstant());
        assertEquals(ZonedDateTime.parse("2020-12-16T00:00:00Z"), user.getCreatedAt());
        assertEquals("name test", user.getName());
    }
}
