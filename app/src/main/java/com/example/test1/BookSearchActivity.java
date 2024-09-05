package com.example.test1;

public class BookSearchActivity {
    private String title;
    private String author;
    private String coverPhotoUrl;
    private String publisher;
    private int pages;

    public BookSearchActivity() {
        // Default constructor required for calls to DataSnapshot.getValue(Book.class)
    }

    public BookSearchActivity(String title, String author, String coverPhotoUrl, String publisher, int pages) {
        this.title = title;
        this.author = author;
        this.coverPhotoUrl = coverPhotoUrl;
        this.publisher = publisher;
        this.pages = pages;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPages() {
        return pages;
    }
}
