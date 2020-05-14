package com.example.accountbook.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private ViewPager2 pager;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        pager = root.findViewById(R.id.pager);
        tabLayout = root.findViewById(R.id.tablayout);

        pager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
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
                return 3;
            }
        });
        //tab与下面的viewpager对应
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("DETAILS");
                        break;
                    case 1:
                        tab.setText("CHARTS");
                        break;
                    case 2:
                        tab.setText("GROUP");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
        return root;
    }
}
