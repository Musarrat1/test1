package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        editTextEmail = findViewById(R.id.et_register_email);
        editTextPassword = findViewById(R.id.et_register_password);
        progressBar = findViewById(R.id.progress_bar);
        authProfile = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        // Register button click listener
        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // Validate user input
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    editTextEmail.setError("Valid email is required");
                    editTextEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(email, password);
                }
            }
        });
    }

    private void registerUser(String email, String password) {
        authProfile.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    if (firebaseUser != null) {
                        storeUserData(firebaseUser); // Assuming new users are not admins
                    }
                } else {
                    handleRegistrationErrors(task.getException());
                }
            }
        });
    }

    private void handleRegistrationErrors(Exception exception) {
        if (exception != null) {
            if (exception instanceof FirebaseAuthWeakPasswordException) {
                Toast.makeText(RegisterActivity.this, "Weak password: " + ((FirebaseAuthWeakPasswordException) exception).getReason(), Toast.LENGTH_SHORT).show();
            } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(RegisterActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
            } else if (exception instanceof FirebaseAuthUserCollisionException) {
                Toast.makeText(RegisterActivity.this, "Email already in use", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Registration failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void storeUserData(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("email", firebaseUser.getEmail());
        user.put("isAdmin", false);

        DocumentReference documentReference = db.collection("Users").document(userId);
        documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, UserProfileActivity.class));
                    finish(); // Close RegisterActivity
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}