package com.example.test1;

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

public class DeleteBookActivity extends AppCompatActivity {

    private DatabaseReference booksRef;
    private List<Book> bookList;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_book);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Delete Book");
        }

        booksRef = FirebaseDatabase.getInstance().getReference("books");

        RecyclerView recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));
        bookList = new ArrayList<>();
        adapter = new BookAdapter(bookList);
        recyclerViewBooks.setAdapter(adapter);

        // Set item click listener
        adapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click if needed
            }

            @Override
            public void onItemLongClick(int position) {
                // Handle long click for delete
                deleteBook(position);
            }
        });

        fetchBooks();
    }

    private void fetchBooks() {
        Query query = booksRef.orderByChild("title"); // Adjust orderByChild as per your database structure
        query.addValueEventListener(new ValueEventListener() {
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DeleteBookActivity.this, "Failed to fetch books", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteBook(int position) {
        Book selectedBook = bookList.get(position);
        String bookIdToDelete = selectedBook.getBookId();
        booksRef.child(bookIdToDelete).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(DeleteBookActivity.this, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DeleteBookActivity.this, "Failed to delete book", Toast.LENGTH_SHORT).show();
                });
    }
}
