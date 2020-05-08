package com.example.accountbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountbook.InsertFlowActivity;
import com.example.accountbook.R;
import com.example.accountbook.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> itemsList;
    private Context context;
    private boolean isChecked = false;
    private View view;

    public CategoryAdapter(Context context, List<Category> itemsList, View view){
        this.context = context;
        this.itemsList = itemsList;
        this.view = view;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.category_item_popup_windows, parent, false);
        return new CategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.ViewHolder holder, final int position) {
        holder.itemsNameTextView.setText(itemsList.get(position).getName());
        // 需要更改
        holder.imageView.setImageResource(itemsList.get(position).getChecked() ? R.mipmap.icon_selected : R.mipmap.icon_unselected);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChecked && !itemsList.get(position).getChecked()){
                    holder.imageView.setImageResource(R.mipmap.icon_selected);
                    isChecked = true;
                    itemsList.get(position).setChecked(true);
                    TextView temp = view.findViewById(R.id.add_category_name);
                    temp.setText(itemsList.get(position).getName());
                    InsertFlowActivity.setCategory_id(itemsList.get(position).getId());
                    return;
                }
                if (itemsList.get(position).getChecked()){
                    holder.imageView.setImageResource(R.mipmap.icon_unselected);
                    isChecked = false;
                    itemsList.get(position).setChecked(false);
                    TextView temp = view.findViewById(R.id.add_category_name);
                    temp.setText("Please select");
                    InsertFlowActivity.setCategory_id(null);
                    return;
                }
//                holder.imageView.setImageResource(itemsList.get(position).getChecked() ? R.mipmap.icon_selected : R.mipmap.icon_unselected);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //obtain the name and image
        TextView itemsNameTextView;
        ImageView imageView;
        RelativeLayout relativeLayout;

        //bind the components
        public ViewHolder (View view) {
            super(view);
            itemsNameTextView = view.findViewById(R.id.textView_category_item);
            imageView = view.findViewById(R.id.image_category_check);
            relativeLayout = view.findViewById(R.id.add_relative_item);
        }
    }
}
