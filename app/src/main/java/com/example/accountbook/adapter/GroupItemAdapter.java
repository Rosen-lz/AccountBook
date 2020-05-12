package com.example.accountbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.R;
import com.example.accountbook.model.DayGroup;


public class GroupItemAdapter extends RecyclerView.Adapter<GroupItemAdapter.ViewHolder>{
    private DayGroup data;
    private Context context;

    public GroupItemAdapter(Context context, DayGroup data) {
        this.context = context;
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //obtain the name and image
        TextView type, money;

        //bind the components
        public ViewHolder (View view) {
            super(view);
            type = view.findViewById(R.id.group_item_type);
            money = view.findViewById(R.id.group_item_money);
        }
    }

    @NonNull
    @Override
    public GroupItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.group_recycler2_item, parent, false);
        return new GroupItemAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupItemAdapter.ViewHolder holder, int position) {
        holder.type.setText(data.getType(position));
        holder.money.setText(data.getMoney(position));
    }

    @Override
    public int getItemCount() {
        return data.getMemberCount();
    }
}
