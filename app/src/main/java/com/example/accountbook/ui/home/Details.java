package com.example.accountbook.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.accountbook.DisplayFlowInfo;
import com.example.accountbook.MyApplication;
import com.example.accountbook.R;
import com.example.accountbook.adapter.DetailsAdapter;
import com.example.accountbook.model.FlowData;
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DetailsAdapter mAdapter;
    private View view;
    private RecyclerView itemRecyclerView;

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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        RefreshLayout refreshLayout = (RefreshLayout)view.findViewById(R.id.details_refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                initRecyclerView();
            }
        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
//            }
//        });
        itemRecyclerView = view.findViewById(R.id.recycler_details);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        itemRecyclerView.setLayoutManager(layoutManager);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        UserService details = new UserService(MyApplication.getInstance());
        final List<FlowData> mitemList = details.getData();
        if(mitemList == null){
            mAdapter = new DetailsAdapter(null, getContext());
            itemRecyclerView.setAdapter(mAdapter);
            return;
        }
        mAdapter = new DetailsAdapter(mitemList, getContext());
        itemRecyclerView.setAdapter(mAdapter);

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
                startActivity(intent);
            }
        });
    }
}
