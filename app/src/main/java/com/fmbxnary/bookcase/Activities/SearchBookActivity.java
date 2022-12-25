package com.fmbxnary.bookcase.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fmbxnary.bookcase.CaptureAct;
import com.fmbxnary.bookcase.R;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SearchBookActivity extends AppCompatActivity {
    // URL for the Google Books API
    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";

    // View variables
    private Button btnSearch;
    private ImageButton btnScan;
    private EditText etIsbn;

    // Request queue for making API calls
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        initViews();
        requestQueue = Volley.newRequestQueue(this);

        // Set click listeners for scan and search buttons
        btnScan.setOnClickListener(view -> scanCode());
        btnSearch.setOnClickListener(view -> getBookDetails());
    }

    // Initialize views
    private void initViews() {
        btnScan = findViewById(R.id.btn_scan);
        etIsbn = findViewById(R.id.et_isbn);
        btnSearch = findViewById(R.id.btn_search);
    }

    // Make an API call to Google Books to get book details and start the AddBookDetails activity
    private void getBookDetails() {

        // Get the ISBN from the EditText
        String isbn = etIsbn.getText().toString();
        if (isbn.isEmpty()) {
            Toast.makeText(this, "Enter a valid ISBN", Toast.LENGTH_SHORT).show();
            return;
        }

        // Concatenate the API URL with the ISBN
        String url = GOOGLE_BOOKS_API_URL + isbn;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Get the volume info from the response
                        JSONObject volume = response.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");
                        String title = volume.optString("title");
                        String author = Objects.requireNonNull(volume.optJSONArray("authors")).optString(0);
                        int pageCount = volume.optInt("pageCount");
                        String description = volume.optString("description");
                        String publishedDate = volume.optString("publishedDate");

                        // Get the second industry identifier (ISBN code)
                        JSONObject isbnList = Objects.requireNonNull(volume.optJSONArray("industryIdentifiers")).getJSONObject(1);
                        String isbnCode = isbnList.optString("identifier");
                        String thumbnailUrl = "";
                        if (volume.optJSONObject("imageLinks") != null) {
                            thumbnailUrl = volume.getJSONObject("imageLinks").getString("smallThumbnail").replaceAll("http:", "https:");
                        }

                        // Create an intent to start the AddBookDetails activity
                        Intent intent = new Intent(this, AddBookActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("description", description);
                        intent.putExtra("author", author);
                        intent.putExtra("pageCount", pageCount);
                        intent.putExtra("thumbnail_url", thumbnailUrl);
                        intent.putExtra("publishedDate", publishedDate);
                        intent.putExtra("isbn", isbnCode);
                        startActivity(intent);
                    } catch (JSONException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                });
        requestQueue.add(request);
    }

    // Open camera method for scan ISBN
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    // Set ISBN to the EditText if ISBN scanned
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            etIsbn.setText(result.getContents());
        }
    });
}