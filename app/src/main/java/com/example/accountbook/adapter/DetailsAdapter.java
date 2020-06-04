package com.example.accountbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.R;
import com.example.accountbook.model.FlowData;
import com.example.accountbook.service.UserService;
import com.example.accountbook.ui.home.Details;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
    //store a member variable for the News
    private OnItemClickListener mOnItemClickListener;
    private List<FlowData> itemsList;
    private Context context;

    //define a interface to implement the itemOnClick function
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //allow the main activity to use this interface
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    // provide a suitable constructor
    public DetailsAdapter(List<FlowData> itemsList, Context context){
        this.context = context;
        this.itemsList = itemsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //obtain the name and image
        TextView itemsNameTextView, itemsMakeDate, itemsMoney, deleteItem;
        RelativeLayout relativeLayout;
        //bind the components
        public ViewHolder (View view) {
            super(view);
            relativeLayout = view.findViewById(R.id.details_layout);
            deleteItem = view.findViewById(R.id.details_item_delete);
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
        holder.itemsNameTextView.setText(itemsList.get(position).getCategory());
        if (itemsList.get(position).isCost()){
            holder.itemsMoney.setText("- " + itemsList.get(position).getMoney());
        }else{
            holder.itemsMoney.setText("+ " + itemsList.get(position).getMoney());
        }
        holder.itemsMakeDate.setText(itemsList.get(position).getMakeDate());
        //set the itemView onclick function
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService details = new UserService(context);
                details.deleteData(itemsList.get(position).getFlow_id());
                //call the broadcast receiver in the Details fragment
                Intent intent = new Intent(Details.ACTION_TAG);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        setEvent(holder);
    }

    @Override
    public int getItemCount() {
        return itemsList==null ? 0 : itemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private void setEvent(final ViewHolder holder) {
        if (mOnItemClickListener != null){
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.relativeLayout, layoutPosition);
                }
            });
        }
    }
}
