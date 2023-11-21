package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MessageTest {

    @Test
    public void testMessageCreation() {
        String testText = "Hello, World!";
        String testUser = "User123";
        Message message = new Message(testText, testUser);

        assertNotNull("Message object should not be null", message);
        assertEquals("Message text should match", testText, message.getMessageText());
        assertEquals("Message user should match", testUser, message.getMessageUser());
        assertNotNull("Message time should be set", message.getMessageTime());
    }

    @Test
    public void testSettersAndGetters() {
        String testText = "New Message";
        String testUser = "User456";
        Message message = new Message();

        // Set values using setters
        message.setMessageText(testText);
        message.setMessageUser(testUser);
        long currentTime = System.currentTimeMillis();
        message.setMessageTime(currentTime);

        // Verify values using getters
        assertEquals("Message text should match", testText, message.getMessageText());
        assertEquals("Message user should match", testUser, message.getMessageUser());
        assertEquals("Message time should match", currentTime, message.getMessageTime());
    }
}
