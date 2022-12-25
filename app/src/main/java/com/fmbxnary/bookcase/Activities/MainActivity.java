package com.fmbxnary.bookcase.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmbxnary.bookcase.Adapter.BookRecyclerAdapter;
import com.fmbxnary.bookcase.Database.DatabaseHelper;
import com.fmbxnary.bookcase.Model.Book;
import com.fmbxnary.bookcase.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ArrayList<Book> books;
    private RecyclerView recyclerView;
    private BookRecyclerAdapter bookRecyclerAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize member variables
        viewSettings();

        // Fill the books ArrayList with data from the database
        fillTheArray();

        // Set up the RecyclerView and its adapter
        bookRecyclerAdapter.notifyDataSetChanged();

        // Set up the floating action button to open the SearchBookActivity
        FloatingActionButton btn_add_book = findViewById(R.id.btn_add_book);
        btn_add_book.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SearchBookActivity.class);
            startActivity(intent);
            //finish();
        });

        // Set up the ItemTouchHelper for swiping to delete
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.LEFT) { //if swipe left
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //alert for confirm to delete
                    builder.setMessage("Are you sure to delete?"); //set message
                    //when click on DELETE
                    //not removing items if cancel is done
                    builder.setPositiveButton("REMOVE", (dialog, which) -> {
                        bookRecyclerAdapter.notifyItemRemoved(position); //item removed from recyclerview
                        deleteBook(position); // delete book from the database
                        books.remove(position);
                    }).setNegativeButton("CANCEL", (dialog, which) -> {
                        bookRecyclerAdapter.notifyItemRemoved(position + 1); //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                        bookRecyclerAdapter.notifyItemRangeChanged(position, bookRecyclerAdapter.getItemCount()); //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                    }).show(); //show alert dialog
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recyclerview

        bookRecyclerAdapter.setOnItemClickListener(book -> {
            Intent intent = new Intent(getApplicationContext(), DetailsBookActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    private void fillTheArray() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String selectQuery = "SELECT  * FROM " + "Bookcase";
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = new Book();
            book.setIsbn(cursor.getString(0));
            book.setTitle(cursor.getString(1));
            book.setAuthor(cursor.getString(2));
            book.setPageCount(cursor.getInt(3));
            book.setPublishedDate(cursor.getString(4));
            book.setDescription(cursor.getString(5));
            book.setCover(cursor.getBlob(6));
            books.add(book);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();

        recyclerView.setAdapter(bookRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    // Method to set up the RecyclerView and other views
    private void viewSettings() {
        recyclerView = findViewById(R.id.recyclerView);
        books = new ArrayList<>();
        bookRecyclerAdapter = new BookRecyclerAdapter(books);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // Delete book from the database
    public void deleteBook(int bookPosition) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Book book = books.get(bookPosition);
        String isbn = book.getIsbn();
        db.deleteBook(isbn);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Book> newBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                newBooks.add(book);
            }
        }
        bookRecyclerAdapter.setBooks(newBooks);
        bookRecyclerAdapter.notifyDataSetChanged();
        return true;
    }
}