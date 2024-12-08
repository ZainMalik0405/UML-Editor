package com.example.umleditor.data;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ActorTest {

    @Test
    public void testConstructorAndGetters() {
        // Create an instance of Actor using the constructor
        Actor actor = new Actor(10.0, 20.0, 30.0, "Test Actor");

        // Verify the initial values using getters
        assertEquals(10.0, actor.getX(), 0.001);
        assertEquals(20.0, actor.getY(), 0.001);
        assertEquals(30.0, actor.getSize(), 0.001);
        assertEquals("Test Actor", actor.getName());
    }

    @Test
    public void testSetters() {
        // Create an instance of Actor
        Actor actor = new Actor(0.0, 0.0, 0.0, "Initial Name");

        // Set new values using setters
        actor.setX(15.0);
        actor.setY(25.0);
        actor.setSize(35.0);
        actor.setName("Updated Name");

        // Verify the updated values using getters
        assertEquals(15.0, actor.getX(), 0.001);
        assertEquals(25.0, actor.getY(), 0.001);
        assertEquals(35.0, actor.getSize(), 0.001);
        assertEquals("Updated Name", actor.getName());
    }
}
