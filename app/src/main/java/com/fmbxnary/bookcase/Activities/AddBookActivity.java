package com.fmbxnary.bookcase.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fmbxnary.bookcase.Database.DatabaseHelper;
import com.fmbxnary.bookcase.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AddBookActivity extends AppCompatActivity {
    // Declare EditText fields for book details
    private EditText etIsbn, etBookTitle, etBookDesc, etBookPageCount, etBookPublishedDate, etAuthor;
    // Declare ImageView field for book cover image
    private ImageView ivBookImg;
    // Declare Button for adding book to database
    private Button btnAddBookSql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Initialize views
        initViews();

        // Set EditText fields with passed values from previous activity
        setPassedValues();

        // Set onClickListener for add book button
        btnAddBookSql.setOnClickListener(v -> addBookToDatabase());
    }

    private void initViews() {
        // Initialize EditText fields
        etIsbn = findViewById(R.id.book_isbn);
        etBookTitle = findViewById(R.id.book_title);
        etAuthor = findViewById(R.id.book_author);
        etBookDesc = findViewById(R.id.book_description);
        etBookPageCount = findViewById(R.id.book_pageCount);
        etBookPublishedDate = findViewById(R.id.book_publishedDate);

        // Initialize ImageView field
        ivBookImg = findViewById(R.id.book_img);

        // Initialize add book button
        btnAddBookSql = findViewById(R.id.btn_add_book_sql);
    }

    // Method for adding book to database
    private void addBookToDatabase() {
        // Create new DatabaseHelper object
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        // Convert book cover image from ImageView to byte array
        BitmapDrawable drawable = (BitmapDrawable) ivBookImg.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] img = byteArray.toByteArray();

        // Call addBook method on DatabaseHelper object, passing in values from EditText fields and cover image byte array
        db.addBook(
                etIsbn.getText().toString(),
                etBookTitle.getText().toString(),
                etAuthor.getText().toString(),
                Integer.parseInt(etBookPageCount.getText().toString()),
                etBookPublishedDate.getText().toString(),
                etBookDesc.getText().toString(),
                img
        );
    }

    // Method for setting values of EditText fields based on passed values from previous activity
    private void setPassedValues() {
        Intent intent = getIntent();
        String isbn = intent.getStringExtra("isbn");
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String description = intent.getStringExtra("description");
        String pageCount = String.valueOf(intent.getIntExtra("pageCount", 0));
        // Declare variables for storing passed values from previous activity
        String url = intent.getStringExtra("thumbnail_url");
        String publishedDate = intent.getStringExtra("publishedDate");

        // Set values of EditText fields with passed values
        etIsbn.setText(isbn);
        etBookTitle.setText(title);
        etAuthor.setText(author);
        etBookDesc.setText(description);
        etBookPageCount.setText(pageCount);

        // Use Picasso library to load book cover image from URL and display in ImageView
        if (!TextUtils.isEmpty(url)) Picasso.get().load(url).into(ivBookImg);
        etBookPublishedDate.setText(publishedDate);
    }
}