package com.example.test1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;

    // Firebase Database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Profile");
        }

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI elements
        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        Button buttonSave = findViewById(R.id.button_save);

        // Set OnClickListener for the Save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (!name.isEmpty() && !email.isEmpty()) {
            // Generate a unique user ID
            String userId = databaseReference.push().getKey();

            // Create a user object
            UserProfile userProfile = new UserProfile(name, email);

            // Save the user object under the user ID
            databaseReference.child(userId).setValue(userProfile)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Show dialog with the saved name and email
                            showDialog(name, email);
                        } else {
                            Toast.makeText(this, "Failed to save profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(String name, String email) {
        // Build an AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Profile Saved")
                .setMessage("Name: " + name + "\nEmail: " + email)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss(); // Close the dialog
                    finish(); // Close the activity and return to the previous screen
                })
                .setCancelable(false) // Prevent dismissal by clicking outside the dialog
                .show();
    }

    // User profile model class
    public static class UserProfile {
        public String name;
        public String email;

        public UserProfile() {
            // Default constructor required for calls to DataSnapshot.getValue(UserProfile.class)
        }

        public UserProfile(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}
