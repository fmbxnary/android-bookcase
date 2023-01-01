package com.fmbxnary.bookcase.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private byte[] cover;
    private String title;
    private String isbn;
    private String author;
    private int pageCount;
    private String description;
    private String publishedDate;
    private String userNote = "";

    public Book() {

    }

    protected Book(Parcel in) {
        cover = in.createByteArray();
        title = in.readString();
        isbn = in.readString();
        author = in.readString();
        pageCount = in.readInt();
        description = in.readString();
        publishedDate = in.readString();
        userNote = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
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

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(cover);
        dest.writeString(title);
        dest.writeString(isbn);
        dest.writeString(author);
        dest.writeInt(pageCount);
        dest.writeString(description);
        dest.writeString(publishedDate);
        dest.writeString(userNote);
    }
}

