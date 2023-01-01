package com.fmbxnary.bookcase.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmbxnary.bookcase.Database.DatabaseHelper;
import com.fmbxnary.bookcase.Model.Book;
import com.fmbxnary.bookcase.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class DetailsFragment extends Fragment {
    // Declare EditText fields for book details
    private EditText etIsbn, etBookTitle, etBookDesc, etBookPageCount, etBookPublishedDate, etAuthor;
    // Declare ImageView field for book cover image
    private ImageView ivBookImg;
    // Declare Button for adding book to database
    private Button btnSaveBookSql;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        initViews(view);
        // Retrieve the Book object from the arguments
        Bundle data = getArguments();
        Book book = data.getParcelable("book2");

        // Use the Book object as needed
        setPassedValues(book);
        ivBookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        btnSaveBookSql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateToDatabase();
            }
        });
        return view;
    }


    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    getActivity().getContentResolver(),
                                    selectedImageUri);
                            selectedImageBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 57, 80, false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ivBookImg.setImageBitmap(
                                selectedImageBitmap);
                    }
                }
            });

    private void initViews(View view) {
        // Initialize EditText fields
        etIsbn = view.findViewById(R.id.book_isbn);
        etBookTitle = view.findViewById(R.id.book_title);
        etAuthor = view.findViewById(R.id.book_author);
        etBookDesc = view.findViewById(R.id.book_description);
        etBookPageCount = view.findViewById(R.id.book_pageCount);
        etBookPublishedDate = view.findViewById(R.id.book_publishedDate);

        // Initialize ImageView field
        ivBookImg = view.findViewById(R.id.book_img);

        // Initialize add book button
        btnSaveBookSql = view.findViewById(R.id.btn_save_book_sql);
    }

    private void setPassedValues(Book book) {
        etIsbn.setText(book.getIsbn());
        etBookTitle.setText(book.getTitle());
        etBookPageCount.setText(String.valueOf(book.getPageCount()));
        etBookDesc.setText(book.getDescription());
        etBookPublishedDate.setText(book.getPublishedDate());
        etAuthor.setText(book.getAuthor());
        Bitmap bmp = BitmapFactory.decodeByteArray(book.getCover(), 0, book.getCover().length);
        ivBookImg.setImageBitmap(bmp);
    }

    private void updateToDatabase() {
        // Create new DatabaseHelper object
        DatabaseHelper db = new DatabaseHelper(getActivity());

        // Convert book cover image from ImageView to byte array
        BitmapDrawable drawable = (BitmapDrawable) ivBookImg.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] img = byteArray.toByteArray();

        // Call addBook method on DatabaseHelper object, passing in values from EditText fields and cover image byte array
        db.updateBook(
                etIsbn.getText().toString(),
                etBookTitle.getText().toString(),
                etAuthor.getText().toString(),
                Integer.parseInt(etBookPageCount.getText().toString()),
                etBookPublishedDate.getText().toString(),
                etBookDesc.getText().toString(),
                img
        );

    }


}