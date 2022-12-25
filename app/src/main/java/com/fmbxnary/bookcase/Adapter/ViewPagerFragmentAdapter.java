package com.fmbxnary.bookcase.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fmbxnary.bookcase.Fragments.DetailsFragment;
import com.fmbxnary.bookcase.Fragments.NoteFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private final String[] titles = new String[]{"Details", "Notes"};
    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new DetailsFragment();
            case 1:
                return new NoteFragment();
        }
        return new DetailsFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
