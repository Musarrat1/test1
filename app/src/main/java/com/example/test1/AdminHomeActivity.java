package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Admin Home");
        }

        Button buttonAddBook = findViewById(R.id.btn_add_book);

        //Button buttonDeleteBook=findViewById(R.id.btn_Delete_book);
        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });

      //  buttonDeleteBook.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View v) {
            //    Intent intent = new Intent(AdminHomeActivity.this, DeleteBookActivity.class);
            //    startActivity(intent);
           // }
        //});
    }
}
