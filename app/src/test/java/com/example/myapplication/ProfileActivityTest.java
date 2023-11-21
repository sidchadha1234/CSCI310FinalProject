package com.example.myapplication;
import org.junit.Before;
import org.junit.Test;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.EditText;


public class ProfileActivityTest {
    private ProfileActivity profileActivity;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        profileActivity = new ProfileActivity();
        // Initialize the EditTexts or mock them
        profileActivity.nameEditText = new EditText(context); // Mock or actual EditText
        profileActivity.uscIdEditText = new EditText(context);
        profileActivity.emailEditText = new EditText(context);
    }

    @Test
    public void testUpdateUIWithUserInfo() {
        profileActivity.updateUIWithUserInfo("TestName", "123456", "test@email.com");

        assertEquals("TestName", profileActivity.nameEditText.getText().toString());
        assertEquals("123456", profileActivity.uscIdEditText.getText().toString());
        assertEquals("test@email.com", profileActivity.emailEditText.getText().toString());
    }
}
