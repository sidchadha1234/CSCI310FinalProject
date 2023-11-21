package com.example.myapplication;
import org.junit.Before;
import org.junit.Test;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileActivityTest {
    private ProfileActivity profileActivity;
    @Mock
    private FirebaseDatabase mockedFirebaseDatabase;
    @Mock
    private DatabaseReference mockedstudentsReference;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockedFirebaseDatabase.getReference("students")).thenReturn(mockedstudentsReference);
        when(mockedstudentsReference.child(anyString())).thenReturn(mockedstudentsReference); // Return the mock for any child path
        when(mockedstudentsReference.push()).thenReturn(mockedstudentsReference); // Return the mock for push()

    }
    @Test
    public void saveUserInfo_savesCorrectDataToFirebase() {}
    public void testUpdateUIWithUserInfo() {
        profileActivity.updateUIWithUserInfo("TestName", "123456", "test@email.com");

        assertEquals("TestName", profileActivity.nameEditText.getText().toString());
        assertEquals("123456", profileActivity.uscIdEditText.getText().toString());
        assertEquals("test@email.com", profileActivity.emailEditText.getText().toString());
    }
}
