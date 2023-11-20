package com.example.myapplication;

import static org.mockito.Mockito.*;

import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class SignUpTest {

    @Mock
    private FirebaseAuthWrapper mockAuthWrapper;
    @Mock
    private Task<AuthResult> mockAuthResultTask;
    private MainActivity mainActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mainActivity = new MainActivity();
        mainActivity.setAuthWrapper(mockAuthWrapper); // You will need to create this setter method
        when(mockAuthWrapper.createUserWithEmailAndPassword(anyString(), anyString()))
                .thenReturn(mockAuthResultTask);
    }

    @Test
    public void signUpWithValidEmailAndPassword() {
        mainActivity.signUp("test@gmail.com", "password123", "Undergraduate");
        verify(mockAuthWrapper).createUserWithEmailAndPassword("test@gmail.com", "password123");
    }
}
