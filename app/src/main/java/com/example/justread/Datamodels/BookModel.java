package com.example.justread.Datamodels;

public class BookModel {
    private String bookname, bookauthor, bookcategory;
    private int notRead, userId, bookId; // 1==> yet to read, 0 ==> read already

    public BookModel(int bookId, String bookname, String bookauthor, String bookcategory, int notRead, int userId) {
        this.bookname = bookname;
        this.bookauthor = bookauthor;
        this.bookcategory = bookcategory;
        this.notRead = notRead;
        this.userId = userId;
        this.bookId = bookId;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookcategory() {
        return bookcategory;
    }

    public void setBookcategory(String bookcategory) {
        this.bookcategory = bookcategory;
    }

    public int getNotRead() {
        return notRead;
    }

    public void setNotRead(int notRead) {
        this.notRead = notRead;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }
}
