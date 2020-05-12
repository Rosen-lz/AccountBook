package com.example.accountbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.R;
import com.example.accountbook.model.DayGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private List<DayGroup> itemsList = new ArrayList<>();
    private Context context;

    public GroupAdapter(Context context, List<DayGroup> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //obtain the name and image
        TextView date;
        RecyclerView recyclerView;

        //bind the components
        public ViewHolder (View view) {
            super(view);
            date = view.findViewById(R.id.group_item_day);
            recyclerView = view.findViewById(R.id.group_recycler2);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    @NonNull
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.group_recycler_item, parent, false);
        return new GroupAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.ViewHolder holder, int position) {
        DayGroup temp = itemsList.get(position);
        holder.date.setText(temp.getDay());

        holder.recyclerView.setAdapter(new GroupItemAdapter(context, temp));
    }

    @Override
    public int getItemCount() {
        return itemsList==null ? 0 : itemsList.size();
    }
}