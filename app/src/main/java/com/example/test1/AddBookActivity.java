
package com.example.test1;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBookActivity extends AppCompatActivity {

    private EditText editTextBookTitle, editTextBookAuthor, editTextBookGenre, editTextBookDescription;
    private ProgressBar progressBar;
    private DatabaseReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Book");
        }

        editTextBookTitle = findViewById(R.id.editText_book_title);
        editTextBookAuthor = findViewById(R.id.editText_book_author);
        editTextBookGenre = findViewById(R.id.editText_book_genre);
        editTextBookDescription = findViewById(R.id.editText_book_description);
        progressBar = findViewById(R.id.progress_bar);

        booksRef = FirebaseDatabase.getInstance().getReference("books");



        Button buttonAddBook = findViewById(R.id.button_addBook);
        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
    }

    private void addBook() {
        String title = editTextBookTitle.getText().toString().trim();
        String author = editTextBookAuthor.getText().toString().trim();
        String genre = editTextBookGenre.getText().toString().trim();
        String description = editTextBookDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            editTextBookTitle.setError("Title is required");
            editTextBookTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(author)) {
            editTextBookAuthor.setError("Author is required");
            editTextBookAuthor.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(genre)) {
            editTextBookGenre.setError("Genre is required");
            editTextBookGenre.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(description)) {
            editTextBookDescription.setError("Description is required");
            editTextBookDescription.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Generate a unique book ID
        String bookId = booksRef.push().getKey();
        Book book = new Book(bookId, title, author, genre, description);

        if (bookId != null) {
            booksRef.child(bookId).setValue(book).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(AddBookActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddBookActivity.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}