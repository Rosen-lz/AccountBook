package com.example.accountbook.ui.information;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.accountbook.R;
import com.example.accountbook.UpdateInfoActivity;
import com.example.accountbook.model.User;
import com.example.accountbook.service.UserService;

public class InfoFragment extends Fragment {
    private User user = null;
    private TextView name, password, email, phone, birthday, sex;
    private View linearLayout_username, linearLayout_password, linearLayout_email, linearLayout_phone, linearLayout_birthday, linearLayout_sex;
    public static final String ACTION_TAG = "Update Info";
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver mReceiver;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info, container, false);
        //bind the view
        bindView(root);
        //register broadcast receiver
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_TAG);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initData();
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
        //initiate data
        initData();
        // bind the ontouch Listener
        settingListener();
        return root;
    }

    private void bindView(View root) {
        name = root.findViewById(R.id.info_username);
        password = root.findViewById(R.id.info_password);
        email = root.findViewById(R.id.info_email);
        phone = root.findViewById(R.id.info_phone);
        birthday = root.findViewById(R.id.info_birthday);
        sex = root.findViewById(R.id.info_sex);
        linearLayout_username = root.findViewById(R.id.linearLayout_username);
        linearLayout_password = root.findViewById(R.id.linearLayout_password);
        linearLayout_email = root.findViewById(R.id.linearLayout_email);
        linearLayout_phone = root.findViewById(R.id.linearLayout_phone);
        linearLayout_birthday = root.findViewById(R.id.linearLayout_birthday);
        linearLayout_sex = root.findViewById(R.id.linearLayout_sex);
    }

    private void initData() {
        UserService temp = new UserService(getContext());
        user = temp.getUserInfo();
        name.setText(user.getUsername());
        password.setText(user.getPassword());
        String tempData = user.getEmail();
        if (tempData.isEmpty()) {
            email.setText("No data");
        }else{
            email.setText(tempData);
        }
        tempData = user.getPhone();
        if (tempData.isEmpty()) {
            phone.setText("No data");
        }else{
            phone.setText(tempData);
        }
        birthday.setText(user.getBirthday());
        sex.setText(user.getSex());
    }

    private void settingListener() {
        linearLayout_username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Toast.makeText(getActivity(), "Username cannot be changed!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        linearLayout_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent(getActivity(), UpdateInfoActivity.class);
                    intent.putExtra("type", "password");
                    intent.putExtra("value", password.getText());
                    intent.putExtra("id", user.getId());
                    startActivity(intent);
                }
                return true;
            }
        });
        linearLayout_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent(getActivity(), UpdateInfoActivity.class);
                    intent.putExtra("type", "email");
                    intent.putExtra("value", email.getText());
                    intent.putExtra("id", user.getId());
                    startActivity(intent);
                }
                return true;
            }
        });
        linearLayout_phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent(getActivity(), UpdateInfoActivity.class);
                    intent.putExtra("type", "phone");
                    intent.putExtra("value", phone.getText());
                    intent.putExtra("id", user.getId());
                    startActivity(intent);
                }
                return true;
            }
        });
        linearLayout_birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent(getActivity(), UpdateInfoActivity.class);
                    intent.putExtra("type", "birthday");
                    intent.putExtra("value", birthday.getText());
                    intent.putExtra("id", user.getId());
                    startActivity(intent);
                }
                return true;
            }
        });
        linearLayout_sex.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent(getActivity(), UpdateInfoActivity.class);
                    intent.putExtra("type", "sex");
                    intent.putExtra("value", sex.getText());
                    intent.putExtra("id", user.getId());
                    startActivity(intent);
                }
                return true;
            }
        });
    }
}
