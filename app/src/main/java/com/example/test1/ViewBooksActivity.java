package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBooks;
    private BookAdapterActivity bookAdapter;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("View Books");
        }

        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapterActivity(bookList);
        recyclerViewBooks.setAdapter(bookAdapter);

        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("books");
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        book.setBookId(bookSnapshot.getKey()); // Set the book ID
                        bookList.add(book);
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewBooksActivity.this, "Failed to load books", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle item click or delete action in RecyclerView
        bookAdapter.setOnItemClickListener(new BookAdapterActivity.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here, if needed
            }

            @Override
            public void onDeleteClick(int position) {
                Book selectedBook = bookList.get(position);
                startDeleteActivity(selectedBook.getBookId()); // Pass book ID instead of title
            }

        });
    }

    private void startDeleteActivity(String bookTitle) {
        Intent intent = new Intent(ViewBooksActivity.this, DeleteBookActivity.class);
        intent.putExtra("bookTitle", bookTitle); // Pass book title as intent extra
        startActivity(intent);
    }
}
