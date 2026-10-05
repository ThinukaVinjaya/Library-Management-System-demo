package com.library.model;


public class Book {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private String availabilityStatus; // "Available" or "Borrowed"

    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_BORROWED = "Borrowed";

    // Default Constructor
    public Book() {
        this.availabilityStatus = STATUS_AVAILABLE;
    }

    // Parameterized Constructor
    public Book(String bookId, String title, String author, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.availabilityStatus = STATUS_AVAILABLE;
    }

    // Full Parameterized Constructor
    public Book(String bookId, String title, String author, String category, String availabilityStatus) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters and Setters 
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    // Business Methods
    public boolean isAvailable() {
        return STATUS_AVAILABLE.equalsIgnoreCase(this.availabilityStatus);
    }

    public void markAsBorrowed() {
        this.availabilityStatus = STATUS_BORROWED;
    }

    public void markAsAvailable() {
        this.availabilityStatus = STATUS_AVAILABLE;
    }

    @Override
    public String toString() {
        return bookId + " - " + title + " (" + author + ")";
    }
}
