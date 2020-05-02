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
import androidx.viewpager2.widget.ViewPager2;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private ViewPager2 pager;

    private HomeViewModel homeViewModel;

    private TabLayout tabLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        pager = root.findViewById(R.id.pager);
        tabLayout = root.findViewById(R.id.tablayout);

        pager.setAdapter(new FragmentAdapter(this));
        //the method below can be used to set the tab name
//        new TabLayoutMediator(tabLayout, pager, true,new TabLayoutMediator.TabConfigurationStrategy(){
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                tab.setText("hello "+position);
//            }
//        }).attach();
        return root;
    }
}
