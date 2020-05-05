package com.example.accountbook.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.R;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.NewsViewHolder> {
    //store a member variable for the News
    private List<DetailsItem> itemsList;
    private Context context;

    //define a interface to implement the itemOnClick function
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // provide a suitable constructor
    public DetailsAdapter(List<com.example.accountbook.ui.home.DetailsItem> itemsList, Context context){
        this.context = context;
        this.itemsList = itemsList;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        //obtain the name and image
        TextView itemsNameTextView, itemsMakeDate, itemsMoney;

        //bind the components
        public NewsViewHolder (View view) {
            super(view);
            itemsNameTextView = view.findViewById(R.id.textView_type);
            itemsMakeDate = view.findViewById(R.id.textView_makeDate);
            itemsMoney = view.findViewById(R.id.textView_money);
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.details_items_layout, parent, false);
        return new DetailsAdapter.NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int position) {
        //display the data according to the position
        holder.itemsNameTextView.setText(itemsList.get(position).getType());
        holder.itemsMoney.setText(itemsList.get(position).getMoney().toString());
        holder.itemsMakeDate.setText(itemsList.get(position).getMakeDate());
        //set the itemView onclick function
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
