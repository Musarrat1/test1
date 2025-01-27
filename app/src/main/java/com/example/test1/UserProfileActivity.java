// UserProfileActivity.java
package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("User  Profile");
        }

        // Initialize View Books button
        Button buttonViewBooks = findViewById(R.id.button_view_books);
        buttonViewBooks.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, ViewBooksActivity.class);
            startActivity(intent);
        });

        // Initialize Edit Profile button
        Button buttonEditProfile = findViewById(R.id.button_edit_profile);
        buttonEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        Button wishlistButton = findViewById(R.id.button_wishlist);
        wishlistButton.setOnClickListener(view -> {
            // Open Wishlist Activity or perform desired action
            Intent intent = new Intent(UserProfileActivity.this, WishlistActivity.class);
            startActivity(intent);
        });
    }
}