package com.example.accountbook.ui.home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class FragmentAdapter extends FragmentStateAdapter {
    private int NUM_PAGES = 3;
    // FragmentActivity
    public FragmentAdapter(Fragment fragment) {
        super(fragment);
    }

    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                return new Details();

            case 1:
                return new Charts();

            default:
                return new Group();

        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
