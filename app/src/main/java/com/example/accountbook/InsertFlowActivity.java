package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.accountbook.adapter.CategoryAdapter;
import com.example.accountbook.model.Category;
import com.example.accountbook.service.DensityUtil;
import com.example.accountbook.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InsertFlowActivity extends AppCompatActivity {

    private PopupWindow popupWindow;
    private CategoryAdapter categoryAdapter;
    private RadioGroup isCost;
    private EditText add_location, add_money, add_note;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_flowctivity);
        Button btn_cancel = findViewById(R.id.button_insert_cancel);
        Button btn_ok = findViewById(R.id.button_insert_ok);
        Button btn_location = findViewById(R.id.button_location);
        add_location = findViewById(R.id.add_location);
        add_money = findViewById(R.id.add_money);
        add_note = findViewById(R.id.add_note);
        isCost = findViewById(R.id.add_isCost);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertFlowActivity.this.finish();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserService user = new UserService(InsertFlowActivity.this);
                Integer category_id = categoryAdapter.getCategoryID();
                String location = add_location.getText().toString().trim();
                Boolean isCost_boolean = ((RadioButton)InsertFlowActivity.this.findViewById(isCost.getCheckedRadioButtonId())).getText().toString().equals("Cost");
                String note = add_note.getText().toString().trim();
                String money = add_money.getText().toString().trim();
                Integer userId = user.getUserID();
                Date date = new Date(System.currentTimeMillis());
                String makeDate = simpleDateFormat.format(date);
//                user.insertFlow(userId, category_id, money, note, makeDate, isCost_boolean, location);
            }
        });

        LinearLayout linearLayout = findViewById(R.id.add_container);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindows(v);
            }
        });
    }

    private void showPopupWindows(View v){
//         下拉框
        UserService details = new UserService(MyApplication.getInstance());
        final List<Category> mitemList = details.getCategory();

        View view = LayoutInflater.from(this).inflate(R.layout.category_popup_windows,null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_category_selector);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView temp = findViewById(R.id.add_category_name);
        categoryAdapter = new CategoryAdapter(this, mitemList, temp);
        recyclerView.setAdapter(categoryAdapter);
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_slow));
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.list_top_in));
        popupWindow = new PopupWindow(view, DensityUtil.dip2px(this, 220), DensityUtil.dip2px(this, 140), true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.anim.fade_in_slow);
        popupWindow.setTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(v);
        popupWindow.showAtLocation(v, Gravity.TOP, 0, 0);
    }
}
