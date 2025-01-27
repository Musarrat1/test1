package com.example.test1;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WishlistAdapter wishlistAdapter;
    private ArrayList<String> wishlist;
    private FirebaseFirestore db;
    private CollectionReference wishlistRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        wishlistRef = db.collection("wishlist");

        // Initialize wishlist
        wishlist = new ArrayList<>();

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        wishlistAdapter = new WishlistAdapter(wishlist);
        recyclerView.setAdapter(wishlistAdapter);

        // Load wishlist from Firestore
        loadWishlistFromFirestore();

        // Handle item clicks for deletion
        wishlistAdapter.setOnItemClickListener(position -> {
            String bookName = wishlist.get(position);
            deleteBookFromFirestore(bookName, position);
        });

        // Add book FAB functionality
        FloatingActionButton fabAddBook = findViewById(R.id.fab_add_book);
        fabAddBook.setOnClickListener(view -> showAddBookDialog());
    }

    private void loadWishlistFromFirestore() {
        wishlistRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    wishlist.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String bookName = documentSnapshot.getString("bookName");
                        if (bookName != null) {
                            wishlist.add(bookName);
                        }
                    }
                    wishlistAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error getting documents: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showAddBookDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Add a Book");

        final EditText input = new EditText(this);
        input.setHint("Enter book name");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String bookName = input.getText().toString().trim();
            if (!bookName.isEmpty()) {
                addBookToFirestore(bookName);
            } else {
                Toast.makeText(this, "Book name cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addBookToFirestore(String bookName) {
        wishlistRef.add(new Book(bookName))
                .addOnSuccessListener(documentReference -> {
                    wishlist.add(bookName);
                    wishlistAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Book added to wishlist!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding book: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deleteBookFromFirestore(String bookName, int position) {
        wishlistRef.whereEqualTo("bookName", bookName).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        queryDocumentSnapshots.getDocuments().get(0).getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    wishlist.remove(position);
                                    wishlistAdapter.notifyItemRemoved(position);
                                    Toast.makeText(this, "Book deleted successfully!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Error deleting book: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Book not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error finding book: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public static class Book {
        private String bookName;

        public Book(String bookName) {
            this.bookName = bookName;
        }

        public String getBookName() {
            return bookName;
        }

        public void setBookName(String bookName) {
            this.bookName = bookName;
        }
    }
}
