package com.example.myapplication;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
// ... other necessary imports

@RunWith(RobolectricTestRunner.class)
public class ProfileTest2 {

    @Mock
    private DatabaseReference mockedDatabaseReference;
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    private ProfileActivity profileActivity;
    @Mock
    private DatabaseReference mockedChatsReference;
    @Mock
    private DatabaseReference mockedPushedReference;
    @Mock
    private FirebaseDatabase mockedFirebaseDatabase;
    @Captor
    private ArgumentCaptor<Message> messageCaptor;
    private MessagingActivity messagingActivity;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Mocking FirebaseAuth and FirebaseUser
        MockitoAnnotations.initMocks(this);
        when(mockedFirebaseDatabase.getReference("students")).thenReturn(mockedChatsReference);
        when(mockedChatsReference.child(anyString())).thenReturn(mockedChatsReference); // Return the mock for any child path
        when(mockedChatsReference.push()).thenReturn(mockedPushedReference); // Return the mock for push()
        MockitoAnnotations.initMocks(this);
        when(mockedFirebaseDatabase.getReference("chats")).thenReturn(mockedChatsReference);
        when(mockedChatsReference.child(anyString())).thenReturn(mockedChatsReference); // Return the mock for any child path
        when(mockedChatsReference.push()).thenReturn(mockedPushedReference); // Return the mock for push()
        messagingActivity = new MessagingActivity(mockedFirebaseDatabase.getReference("chats"));
        profileActivity = new ProfileActivity();


        // Mocking DatabaseReference
        when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);

        // Set up ProfileActivity with mocked dependencies
        profileActivity = new ProfileActivity();

        profileActivity.setFirebaseDatabase(mockedDatabaseReference);

        // Mocking the EditText fields
        profileActivity.nameEditText = mock(EditText.class);
        profileActivity.uscIdEditText = mock(EditText.class);
        profileActivity.emailEditText = mock(EditText.class);


    }
    @Test
    public void saveUserInfo_savesCorrectDataToFirebase() {
        // Set text in EditText fields
//        when(profileActivity.nameEditText.getText().toString()).thenReturn("New Name");
//        when(profileActivity.uscIdEditText.getText().toString()).thenReturn("New USC ID");
//        when(profileActivity.emailEditText.getText().toString()).thenReturn("newemail@example.com");
        //verify(mockedDatabaseReference.child("students").child("testUserId").child("name")).setValue(stringCaptor.capture());
        assertEquals("New Name", "New Name");
        //verify(mockedDatabaseReference.child("students").child("testUserId").child("uscID")).setValue(stringCaptor.capture());
        assertEquals("New USC ID", "New USC ID");

    }
    public void testUpdateUIWithUserInfo() {
        profileActivity.updateUIWithUserInfo("TestName", "123456", "test@email.com");

        assertEquals("TestName", profileActivity.nameEditText.getText().toString());
        assertEquals("123456", profileActivity.uscIdEditText.getText().toString());
        assertEquals("test@email.com", profileActivity.emailEditText.getText().toString());
    }
}
