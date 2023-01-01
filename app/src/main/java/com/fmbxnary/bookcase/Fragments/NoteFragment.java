package com.fmbxnary.bookcase.Fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fmbxnary.bookcase.Database.DatabaseHelper;
import com.fmbxnary.bookcase.Model.Book;
import com.fmbxnary.bookcase.R;

import java.io.ByteArrayOutputStream;


public class NoteFragment extends Fragment {
    EditText userNote;
    Button saveUserNote;
    String isbn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        userNote = view.findViewById(R.id.userNote);
        saveUserNote = view.findViewById(R.id.btn_save_note_sql);
        Bundle data = getArguments();
        Book book = data.getParcelable("book2");
        userNote.setText(book.getUserNote());
        isbn = book.getIsbn();

        saveUserNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNoteToDatabase();
            }
        });

        return view;

    }

    private void saveNoteToDatabase() {
        // Create new DatabaseHelper object
        DatabaseHelper db = new DatabaseHelper(getActivity());

        // Call addBook method on DatabaseHelper object, passing in values from EditText fields and cover image byte array
        db.saveNote(isbn,
                userNote.getText().toString()
        );

    }
}