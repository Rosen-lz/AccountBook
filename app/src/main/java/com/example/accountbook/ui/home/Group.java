package com.example.accountbook.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accountbook.R;
import com.example.accountbook.adapter.GroupAdapter;
import com.example.accountbook.model.DayGroup;
import com.example.accountbook.model.FlowData;
import com.example.accountbook.service.UserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Group#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Group extends Fragment {
    // TODO: Rename and change types of parameters
    private TextView date, cost, income;
    private View view;
    private LinearLayout linearLayout;
    private RecyclerView itemRecyclerView;
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver mReceiver;
    public static final String ACTION_TAG = "Details-Data-Update";

    public Group() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Group.
     */
    // TODO: Rename and change types and number of parameters
    public static Group newInstance(String param1, String param2) {
        Group fragment = new Group();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_group, container, false);
        linearLayout = view.findViewById(R.id.linearLayout_group_date);
        date = view.findViewById(R.id.group_date);
        cost = view.findViewById(R.id.group_cost);
        income = view.findViewById(R.id.group_income);
        // setting recyclerView
        itemRecyclerView = view.findViewById(R.id.group_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        itemRecyclerView.setLayoutManager(layoutManager);

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

        // Calendar
        final Calendar calendar= Calendar.getInstance();
        date.setText(calendar.get(Calendar.YEAR)+"-"+String.format("%02d",calendar.get(Calendar.MONTH)+1));
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String text = "You have selected：" + year + "-" + String.format("%02d", month + 1);
                        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT ).show();
                        date.setText(year + "-" + String.format("%02d", month+1));
                        initRecyclerView();;
                    }
                }
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH));
                mDialog.show();
                ((ViewGroup) ((ViewGroup) mDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                //((ViewGroup) ((ViewGroup) mDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            }
        });

        initRecyclerView();;
        return view;
    }

    private void initRecyclerView() {
        UserService user = new UserService(getContext());
        List<FlowData> mitemList = user.getMonthDate(date.getText().toString().trim());
        if(mitemList == null){
            Toast.makeText(getContext(), "No data in this month", Toast.LENGTH_LONG).show();
            cost.setText("0.0");
            income.setText("0.0");

            itemRecyclerView.setAdapter(new GroupAdapter(getContext(), null));
            return;
        }

        List<DayGroup> data = new ArrayList<>();
        Double total_cost = 0.0;
        Double total_income = 0.0;
        List<String> day = new ArrayList<>();
        for (FlowData temp : mitemList){
            String makeDate = temp.getMakeDate();
            String money = temp.getMoney();
            // calculate the sum of cost and income
            Double cost = 0.0;
            Double income = 0.0;
            String type;
            if (temp.isCost()){
                cost = Double.parseDouble(money);
                total_cost += cost;
                money = "- " + money;
                type = "Cost";
            }else{
                income = Double.parseDouble(money);
                total_income += income;
                money = "+ " + money;
                type = "Income";
            }
            // initialize the recyclerView data
            if (day.contains(makeDate)){
                int position = day.indexOf(makeDate);
                data.get(position).addMember(temp.getFlow_id(), cost, income, type, temp.getCategory(), money, temp.getNote(), temp.getLocation());
            }else{
                day.add(makeDate);
                data.add(new DayGroup(temp.getFlow_id(), cost, income, makeDate, type, temp.getCategory(), money, temp.getNote(), temp.getLocation()));
            }
        }
        cost.setText(total_cost.toString());
        income.setText(total_income.toString());

        itemRecyclerView.setAdapter(new GroupAdapter(getContext(), data));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
    }
}
