package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ListItemTest {

    @Test
    public void testListItemCreationAndAccessors() {
        // Create a ListItem instance with TYPE_COURSE and a sample text
        ListItem listItemCourse = new ListItem(ListItem.TYPE_COURSE, "Sample Course");

        // Assert that the type is correctly set and retrieved
        assertEquals("Type should be TYPE_COURSE", ListItem.TYPE_COURSE, listItemCourse.getType());

        // Assert that the text is correctly set and retrieved
        assertEquals("Text should be 'Sample Course'", "Sample Course", listItemCourse.getText());

        // Repeat the process for TYPE_USER
        ListItem listItemUser = new ListItem(ListItem.TYPE_USER, "Sample User");

        // Assert that the type is correctly set and retrieved for TYPE_USER
        assertEquals("Type should be TYPE_USER", ListItem.TYPE_USER, listItemUser.getType());

        // Assert that the text is correctly set and retrieved for TYPE_USER
        assertEquals("Text should be 'Sample User'", "Sample User", listItemUser.getText());
    }
}
