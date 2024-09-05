package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class First_pageActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        db = FirebaseFirestore.getInstance(); // Initialize Firestore);

        Button buttonLogin = findViewById(R.id.button_login);
        Button buttonRegister = findViewById(R.id.button_register);
        Button button_admin_login = findViewById(R.id.button_admin_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("The Book Heaven");
        }

        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(First_pageActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        button_admin_login.setOnClickListener(v -> {
            Intent intent = new Intent(First_pageActivity.this, AdminRegisterActivity.class);
            startActivity(intent);
        });

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(First_pageActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
