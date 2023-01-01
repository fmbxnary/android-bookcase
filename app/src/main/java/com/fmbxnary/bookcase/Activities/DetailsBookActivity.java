package com.fmbxnary.bookcase.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fmbxnary.bookcase.Adapter.ViewPagerFragmentAdapter;
import com.fmbxnary.bookcase.Fragments.DetailsFragment;
import com.fmbxnary.bookcase.Fragments.NoteFragment;
import com.fmbxnary.bookcase.Model.Book;
import com.fmbxnary.bookcase.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DetailsBookActivity extends AppCompatActivity {
    ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    private final String[] titles = new String[]{"Details", "Notes"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_book);
        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(this);

        // Get the Book object that you want to pass to the fragment
        Book book = getIntent().getParcelableExtra("book");

        // Create a new instance of the fragment and set the Book object as an argument
        DetailsFragment detailsFragment = new DetailsFragment();
        NoteFragment noteFragment = new NoteFragment();
        Bundle data = new Bundle();
        data.putParcelable("book2", book);

        detailsFragment.setArguments(data);
        noteFragment.setArguments(data);


        // Add the fragment to the adapter and set it on the view pager
        viewPagerFragmentAdapter.addFragment(detailsFragment);
        viewPagerFragmentAdapter.addFragment(noteFragment);
        viewPager2.setAdapter(viewPagerFragmentAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, ((tab, position) -> tab.setText(titles[position]))).attach();
    }
}