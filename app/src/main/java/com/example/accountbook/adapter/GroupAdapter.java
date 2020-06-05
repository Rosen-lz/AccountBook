package com.example.accountbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.DisplayFlowInfo;
import com.example.accountbook.MainActivity;
import com.example.accountbook.R;
import com.example.accountbook.model.DayGroup;
import com.example.accountbook.model.SlideRecyclerView;

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
        SlideRecyclerView recyclerView;

        //bind the components
        public ViewHolder (View view) {
            super(view);
            date = view.findViewById(R.id.group_item_day);
            recyclerView = (SlideRecyclerView) view.findViewById(R.id.group_recycler2);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            itemDecoration.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider_inset));
            recyclerView.addItemDecoration(itemDecoration);
        }
    }

    @NonNull
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.group_recycler_item, parent, false);
        return new GroupAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.ViewHolder holder, final int dayPosition) {
        final DayGroup temp = itemsList.get(dayPosition);
        holder.date.setText(temp.getDay());
        GroupItemAdapter groupItemAdapter = new GroupItemAdapter(context, temp);
        holder.recyclerView.setAdapter(groupItemAdapter);
        groupItemAdapter.setOnItemClickListener(new GroupItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(context, DisplayFlowInfo.class);
//                Bundle b=new Bundle();
//                b.putStringArray();
                intent.putExtra("type", itemsList.get(dayPosition).getType(position));
                intent.putExtra("category", itemsList.get(dayPosition).getCategory(position));
                intent.putExtra("money", itemsList.get(dayPosition).getMoney(position).substring(2));
                intent.putExtra("date", temp.getDay());
                intent.putExtra("note", itemsList.get(dayPosition).getNote(position));
                intent.putExtra("location", itemsList.get(dayPosition).getLocation(position));
                ((MainActivity)context).startActivityForResult(intent, 4);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList==null ? 0 : itemsList.size();
    }
}
