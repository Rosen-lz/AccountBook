package com.example.accountbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
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
import com.example.accountbook.Util.DensityUtil;
import com.example.accountbook.service.UserService;
import com.example.accountbook.ui.home.Details;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InsertFlowActivity extends AppCompatActivity implements SpellCheckerSession.SpellCheckerSessionListener {

    private PopupWindow popupWindow;
    private CategoryAdapter categoryAdapter;
    private RadioGroup isCost;
    private EditText add_money, add_note, add_date;
    private TextView add_location, spellchecker, textview_description;
    private List<Category> mitemList;
    private Integer userId;
    private UserService user;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private SpellCheckerSession mScs;


    private static Integer category_id = null;
    public static void setCategory_id(Integer category_id) {
        InsertFlowActivity.category_id = category_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_flowctivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //bind the view
        Button btn_ok = findViewById(R.id.button_insert_ok);
        Button btn_location = findViewById(R.id.button_location);
        add_location = findViewById(R.id.add_location);
        add_money = findViewById(R.id.add_money);
        add_note = findViewById(R.id.add_note);
        isCost = findViewById(R.id.add_isCost);
        add_date = findViewById(R.id.add_makeDate);
        spellchecker = findViewById(R.id.spellchecker);
        textview_description = findViewById(R.id.textView_description);

        //spellchecker
        textview_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!add_note.getText().toString().trim().isEmpty()) {
                    spellchecker.setText("");
                    if (mScs != null) {
                        //check a word
//                        mScs.getSuggestions(new TextInfo(add_note.getText().toString()), 3);
                        //check some words or a sentence
                        mScs.getSentenceSuggestions(new TextInfo[]{new TextInfo(add_note.getText().toString())}, 3);
                    } else {
                        Log.e("TAG_SPELL", "Couldn't obtain the spell checker service.");
                    }
                }else {
                    spellchecker.setText("");
                }
            }
        });

        //access the database
        user = new UserService(InsertFlowActivity.this);
        userId = user.getUserID();
        //get the data
        mitemList = user.getCategory();

        LinearLayout linearLayout = findViewById(R.id.add_container);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindows(v);
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
                    if (add_date.getText().toString().trim().isEmpty()){
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
                        Date date = new Date(System.currentTimeMillis());
                        makeDate = simpleDateFormat.format(date);
                    }else if(!checkDate(add_date.getText().toString().trim())) {
                        Toast.makeText(InsertFlowActivity.this, "The format of your date is wrong",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        makeDate = add_date.getText().toString().trim();
                    }
                    Boolean isCost_boolean = ((RadioButton)InsertFlowActivity.this.findViewById(isCost.getCheckedRadioButtonId())).getText().toString().equals("Cost");
                    String note = add_note.getText().toString().trim();
                    String money = add_money.getText().toString().trim();
                    String location = add_location.getText().toString().trim();
                    // insert data
                    user.insertFlow(userId, category_id, money, note, makeDate, isCost_boolean, location);
                    //call the broadcast receiver in the Details fragment
                    Intent intent = new Intent(Details.ACTION_TAG);
                    LocalBroadcastManager.getInstance(InsertFlowActivity.this).sendBroadcast(intent);
                    // back to MainActivity
                    Intent data = new Intent(InsertFlowActivity.this, MainActivity.class);
                    InsertFlowActivity.this.setResult(RESULT_OK, data);
                    InsertFlowActivity.this.finish();
                }
            }

            private boolean checkDate(String trim) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    dateFormat.setLenient(false);
                    dateFormat.parse(trim);
                    return true;
                }catch (Exception e) {
                    return false;
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
                String text = "You have selected：" + year + "-" + (month + 1) + "-" + dayOfMonth;
                Toast.makeText( InsertFlowActivity.this, text, Toast.LENGTH_SHORT ).show();
                add_date.setText(year + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", dayOfMonth));
            }
        }
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                InsertFlowActivity.this.setResult(RESULT_CANCELED, new Intent(InsertFlowActivity.this, MainActivity.class));
                InsertFlowActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        final TextServicesManager tsm = (TextServicesManager)
                getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        mScs = tsm.newSpellCheckerSession(null, null, this, true);
    }

    public void onPause() {
        super.onPause();
        if (mScs != null) {
            mScs.close();
        }
    }

    @Override
    public void onGetSuggestions(final SuggestionsInfo[] results) {
        final StringBuilder sb = new StringBuilder();
        // Returned suggestions are contained in SuggestionsInfo
        for (int i = 0; i < results.length; i++) {
            // Returned suggestions are contained in SuggestionsInfo
            int len = results[0].getSuggestionsCount();
            sb.append('\t');

            for (int j = 0; j < len; j++) {
                sb.append("," + results[i].getSuggestionAt(j));
            }

            sb.append(" (" + len + ")");
        }

        runOnUiThread(new Runnable() {
            public void run() {
                spellchecker.append(sb.toString());
            }
        });
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        // TODO Auto-generated method stub
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < results.length; i++) {
            SentenceSuggestionsInfo ssi = results[i];
            for (int j = 0; j < ssi.getSuggestionsCount(); ++j) {
                dumpSuggestionsInfoInternal(
                        sb, ssi.getSuggestionsInfoAt(j), ssi.getOffsetAt(j), ssi.getLengthAt(j));
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spellchecker.append(sb.toString());
            }
        });
    }

    private void dumpSuggestionsInfoInternal(
        final StringBuilder sb, final SuggestionsInfo si, final int length, final int offset) {
        // Returned suggestions are contained in SuggestionsInfo
        final int len = si.getSuggestionsCount();
        sb.append('\t');
        for (int j = 0; j < len; ++j) {
            if (j != 0) {
                sb.append(", ");
            }
            sb.append(si.getSuggestionAt(j));
        }
        sb.append(" (" + len + ")");
//        if (length != -1) {
//            sb.append(" length = " + length + ", offset = " + offset);
//        }
    }
}
