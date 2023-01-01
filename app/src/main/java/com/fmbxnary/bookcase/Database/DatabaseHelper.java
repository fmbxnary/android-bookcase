package com.fmbxnary.bookcase.Database;

import android.bluetooth.BluetoothManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbxnary.bookcase.Adapter.BookRecyclerAdapter;

import java.util.concurrent.Executors;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Bookcase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Bookcase";
    private static final String COLUMN_ISBN = "isbn";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_PAGE_COUNT = "page_count";
    private static final String COLUMN_PUBLISH_DATE = "publishDate";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_COVER = "cover";
    private static final String COLUMN_USER_NOTE = "userNote";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ISBN + " VARCHAR PRIMARY KEY, " +
                COLUMN_TITLE + " VARCHAR, " +
                COLUMN_AUTHOR + " VARCHAR, " +
                COLUMN_PAGE_COUNT + " INT, " +
                COLUMN_PUBLISH_DATE + " VARCHAR, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_COVER + " BLOB," +
                COLUMN_USER_NOTE + " TEXT DEFAULT '31haha');";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addBook(String isbn, String title, String author, int pageCount, String publishDate, String description, byte[] cover) {
        Executors.newSingleThreadExecutor().execute(() -> {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ISBN, isbn);
            cv.put(COLUMN_TITLE, title);
            cv.put(COLUMN_AUTHOR, author);
            cv.put(COLUMN_PAGE_COUNT, pageCount);
            cv.put(COLUMN_PUBLISH_DATE, publishDate);
            cv.put(COLUMN_DESCRIPTION, description);
            cv.put(COLUMN_COVER, cover);
            long result = db.insert(TABLE_NAME, null, cv);
            final String toastMessage;
            if (result == -1) {
                toastMessage = "Failed to add book";
            } else {
                toastMessage = "Book added successfully";
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void updateBook(String isbn, String title, String author, int pageCount, String publishDate, String description, byte[] cover) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_ISBN, isbn);
                cv.put(COLUMN_TITLE, title);
                cv.put(COLUMN_AUTHOR, author);
                cv.put(COLUMN_PAGE_COUNT, pageCount);
                cv.put(COLUMN_PUBLISH_DATE, publishDate);
                cv.put(COLUMN_DESCRIPTION, description);
                cv.put(COLUMN_COVER, cover);
                long result = db.update(TABLE_NAME, cv, "isbn=?", new String[]{isbn});

                final String toastMessage;
                if (result == -1) {
                    toastMessage = "Failed to update book";
                } else {
                    toastMessage = "Book updated successfully";
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void deleteBook(String isbn) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                long result = db.delete(TABLE_NAME, "isbn=?", new String[]{isbn});

                final String toastMessage;
                if (result == -1) {
                    toastMessage = "Failed to delete book";
                } else {
                    toastMessage = "Book deleted successfully";
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void saveNote(String isbn, String userNote) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_USER_NOTE, userNote);


                long result = db.update(TABLE_NAME, cv, "isbn=?", new String[]{isbn});

                final String toastMessage;
                if (result == -1) {
                    toastMessage = "Failed to save note";
                } else {
                    toastMessage = "Notes saved successfully";
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
