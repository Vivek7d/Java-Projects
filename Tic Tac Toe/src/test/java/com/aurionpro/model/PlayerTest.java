package com.aurionpro.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setup() {
        player = new Player("Alice");
    }

    @Test
    public void testPlayerNameIsSetCorrectly() {
        assertEquals("Alice", player.getName());
    }

    @Test
    public void testMarkCanBeSetAndRetrieved() {
        player.setMark(MarkType.X);
        assertEquals(MarkType.X, player.getMark());

        player.setMark(MarkType.O);
        assertEquals(MarkType.O, player.getMark());
    }

    @Test
    public void testDefaultMarkIsNullBeforeSetting() {
        assertNull(player.getMark(), "Mark should be null before setting it");
    }
}
