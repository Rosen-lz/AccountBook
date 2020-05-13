package com.example.accountbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.R;
import com.example.accountbook.model.FlowData;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
    //store a member variable for the News
    private List<FlowData> itemsList;
    private Context context;

    // provide a suitable constructor
    public DetailsAdapter(List<FlowData> itemsList, Context context){
        this.context = context;
        this.itemsList = itemsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //obtain the name and image
        TextView itemsNameTextView, itemsMakeDate, itemsMoney;

        //bind the components
        public ViewHolder (View view) {
            super(view);
            itemsNameTextView = view.findViewById(R.id.textView_type);
            itemsMakeDate = view.findViewById(R.id.textView_makeDate);
            itemsMoney = view.findViewById(R.id.textView_money);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.details_items_layout, parent, false);
        return new DetailsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //display the data according to the position
        holder.itemsNameTextView.setText(itemsList.get(position).getType());
        holder.itemsMoney.setText(itemsList.get(position).getMoney().toString());
        holder.itemsMakeDate.setText(itemsList.get(position).getMakeDate());
        //set the itemView onclick function
    }

    @Override
    public int getItemCount() {
        return itemsList==null ? 0 : itemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
