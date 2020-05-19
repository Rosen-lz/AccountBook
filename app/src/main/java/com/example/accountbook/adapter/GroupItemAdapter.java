package com.example.accountbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.R;
import com.example.accountbook.model.DayGroup;
import com.example.accountbook.service.UserService;
import com.example.accountbook.ui.home.Details;


public class GroupItemAdapter extends RecyclerView.Adapter<GroupItemAdapter.ViewHolder>{
    private DayGroup data;
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public GroupItemAdapter(Context context, DayGroup data) {
        this.context = context;
        this.data = data;
    }

    //define a interface to implement the itemOnClick function
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //allow the main activity to use this interface
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //obtain the name and image
        TextView type, money, delete;
        RelativeLayout relativeLayout;

        //bind the components
        public ViewHolder (View view) {
            super(view);
            relativeLayout = view.findViewById(R.id.group_relativeLayout);
            type = view.findViewById(R.id.group_item_type);
            money = view.findViewById(R.id.group_item_money);
            delete = view.findViewById(R.id.group_item_delete);
        }
    }

    @NonNull
    @Override
    public GroupItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.group_recycler2_item, parent, false);
        return new GroupItemAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupItemAdapter.ViewHolder holder, final int position) {
        holder.type.setText(data.getCategory(position));
        holder.money.setText(data.getMoney(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService details = new UserService(context);
                details.deleteData(data.getFlow_id(position));
                //call the broadcast receiver in the Details fragment
                Intent intent = new Intent(Details.ACTION_TAG);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        setEvent(holder);
    }

    @Override
    public int getItemCount() {
        return data.getMemberCount();
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
