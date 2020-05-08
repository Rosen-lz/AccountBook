package com.example.accountbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accountbook.adapter.CategoryAdapter;
import com.example.accountbook.model.Category;
import com.example.accountbook.service.DensityUtil;
import com.example.accountbook.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InsertFlowActivity extends AppCompatActivity {

    private PopupWindow popupWindow;
    private CategoryAdapter categoryAdapter;
    private RadioGroup isCost;
    private EditText add_money, add_note, add_date;
    private TextView add_location;
    private List<Category> mitemList;
    private Integer userId;
    private UserService user;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private static Integer category_id = null;
    public static void setCategory_id(Integer category_id) {
        InsertFlowActivity.category_id = category_id;
    }

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
        add_date = findViewById(R.id.add_makeDate);

        user = new UserService(InsertFlowActivity.this);
        userId = user.getUserID();

        mitemList = user.getCategory();
//        user.insertFlow(1,2,"300","hello","2020/5/8", false, "China");

        LinearLayout linearLayout = findViewById(R.id.add_container);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindows(v);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertFlowActivity.this.setResult(RESULT_CANCELED, new Intent(InsertFlowActivity.this, MainActivity.class));
                InsertFlowActivity.this.finish();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (category_id == null){
                    Toast.makeText(InsertFlowActivity.this, "Please select a category", Toast.LENGTH_SHORT).show();
                }else if(add_money.getText().toString().trim().equals("")){
                    Toast.makeText(InsertFlowActivity.this, "Please enter the money", Toast.LENGTH_SHORT).show();
                }else if(add_note.getText().toString().trim().equals("")){
                    Toast.makeText(InsertFlowActivity.this, "Please enter the description", Toast.LENGTH_SHORT).show();
                } else{
                    String makeDate;
                    if (add_date.getText().toString().trim().equals("")){
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        makeDate = simpleDateFormat.format(date);
                    }else {
                        makeDate = add_date.getText().toString().trim();
                    }
                    Boolean isCost_boolean = ((RadioButton)InsertFlowActivity.this.findViewById(isCost.getCheckedRadioButtonId())).getText().toString().equals("Cost");
                    String note = add_note.getText().toString().trim();
                    String money = add_money.getText().toString().trim();
                    String location = add_location.getText().toString().trim();
                    // insert data
                    user.insertFlow(userId, category_id, money, note, makeDate, isCost_boolean, location);
                    // back to MainActivity
                    Intent data = new Intent(InsertFlowActivity.this, MainActivity.class);
                    InsertFlowActivity.this.setResult(RESULT_OK, data);
                    InsertFlowActivity.this.finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 10000, 15, locationListener);
                }
            }
        }
    }

    private void showPopupWindows(View v){
//         下拉框
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

    // This method is bound with btn_location
    public void getLocation(View view){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                add_location.setText(location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 10000, 15, locationListener);
        }
    }

    public void selectDate(View v){
        Calendar calendar= Calendar.getInstance();
        new DatePickerDialog( this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String text = "You have selected：" + year + "/" + (month + 1) + "/" + dayOfMonth;
                Toast.makeText( InsertFlowActivity.this, text, Toast.LENGTH_SHORT ).show();
                add_date.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
            }
        }
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
