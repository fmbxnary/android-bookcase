package com.fmbxnary.bookcase.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fmbxnary.bookcase.Fragments.DetailsFragment;
import com.fmbxnary.bookcase.Fragments.NoteFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments = new ArrayList<>();

    public ViewPagerFragmentAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
