package com.example.accountbook.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.accountbook.DisplayFlowInfo;
import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.adapter.DetailsAdapter;
import com.example.accountbook.model.FlowData;
import com.example.accountbook.model.SlideRecyclerView;
import com.example.accountbook.service.UserService;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Details extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private DetailsAdapter mAdapter;
    private View view;
    private SlideRecyclerView itemRecyclerView;
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver mReceiver;
    public static final String ACTION_TAG = "Details-Data-Update";

    public Details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Details.
     */
    // TODO: Rename and change types and number of parameters
    public static Details newInstance(String param1, String param2) {
        Details fragment = new Details();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_details, container, false);
        // refresh data
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.details_refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                initRecyclerView();
            }
        });

        // initiate recyclerView
        itemRecyclerView = (SlideRecyclerView) view.findViewById(R.id.recycler_details);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        itemRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_inset));
        itemRecyclerView.addItemDecoration(itemDecoration);
        initRecyclerView();

        //register broadcast receiver
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_TAG);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                initRecyclerView();
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
        return view;
    }

    private void initRecyclerView() {
        UserService details = new UserService(getContext());
        final List<FlowData> mitemList = details.getData();
        if(mitemList == null){
            mAdapter = new DetailsAdapter(null, getContext());
            itemRecyclerView.setAdapter(mAdapter);
            return;
        }
        mAdapter = new DetailsAdapter(mitemList, getContext());
        itemRecyclerView.setAdapter(mAdapter);
        itemRecyclerView.closeMenu();

        mAdapter.setOnItemClickListener(new DetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DisplayFlowInfo.class);
                if (mitemList.get(position).isCost()){
                    intent.putExtra("type", "Cost");
                }else{
                    intent.putExtra("type", "Income");
                }
//                Bundle b=new Bundle();
//                b.putStringArray();
                intent.putExtra("category", mitemList.get(position).getCategory());
                intent.putExtra("money", mitemList.get(position).getMoney());
                intent.putExtra("date", mitemList.get(position).getMakeDate());
                intent.putExtra("note", mitemList.get(position).getNote());
                intent.putExtra("location", mitemList.get(position).getLocation());
                ((MainActivity)getContext()).startActivityForResult(intent,3);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
    }
}
