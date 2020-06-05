package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accountbook.service.UserService;
import com.example.accountbook.ui.information.InfoFragment;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateInfoActivity extends AppCompatActivity {
    private TextView birthday1, birthday2, email1, email2, phone1, phone2, password1, password2, password3;
    private RadioGroup sex;
    private String type;
    private String id;
    private UserService user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        bindView();

        user = new UserService(this);

        type = intent.getStringExtra("type");
        if (type.equals("password")) {
            LinearLayout linearLayout = findViewById(R.id.update_password);
            linearLayout.setVisibility(View.VISIBLE);
            password1.setText(intent.getStringExtra("value"));
        } else if (type.equals("email")){
            LinearLayout linearLayout = findViewById(R.id.update_email);
            linearLayout.setVisibility(View.VISIBLE);
            String temp = intent.getStringExtra("value");
            if (temp.isEmpty()){
                email1.setText("No Email Address");
            } else {
                email1.setText(temp);
            }
        } else if (type.equals("phone")){
            LinearLayout linearLayout = findViewById(R.id.update_phone);
            linearLayout.setVisibility(View.VISIBLE);
            String temp = intent.getStringExtra("value");
            if (temp.isEmpty()){
                phone1.setText("No Phone Number");
            } else {
                phone1.setText(temp);
            }
        } else if (type.equals("sex")){
            LinearLayout linearLayout = findViewById(R.id.update_sex);
            linearLayout.setVisibility(View.VISIBLE);
            String temp = intent.getStringExtra("value");
            if (temp.equals("Female")){
                sex.check(R.id.update_woman);
            }else{
                sex.check(R.id.update_man);
            }
        } else if (type.equals("birthday")){
            LinearLayout linearLayout = findViewById(R.id.update_birthday);
            linearLayout.setVisibility(View.VISIBLE);
            birthday1.setText(intent.getStringExtra("value"));
        }
    }

    private void bindView(){
        birthday1 = findViewById(R.id.update_birthday1);
        birthday2 = findViewById(R.id.update_birthday2);
        email1 = findViewById(R.id.update_email1);
        email2 = findViewById(R.id.update_email2);
        password1 = findViewById(R.id.update_password1);
        password2 =findViewById(R.id.update_password2);
        password3 = findViewById(R.id.update_password3);
        sex = (RadioGroup)findViewById(R.id.update_type);
        phone1 =findViewById(R.id.update_phone1);
        phone2 = findViewById(R.id.update_phone2);
    }

    public void selectDate(View v){
        Calendar calendar= Calendar.getInstance();
        new DatePickerDialog( this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String text = "You have selected：" + year + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", dayOfMonth);
                Toast.makeText( UpdateInfoActivity.this, text, Toast.LENGTH_SHORT ).show();
                birthday2.setText(year + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", dayOfMonth));
            }
        }
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void click(View v){
        boolean isSuccess = false;
        if (type.equals("password")) {
            String temp1 = password2.getText().toString().trim();
            String temp2 = password3.getText().toString().trim();
            if (temp1.isEmpty() || temp2.isEmpty()) {
                Toast.makeText(this, "The password should not be empty",  Toast.LENGTH_SHORT).show();
                return;
            }
            if (!temp1.equals(temp2)) {
                Toast.makeText(this, "Please check your password again", Toast.LENGTH_SHORT).show();
                return;
            }
            if (temp1.equals(password1.getText().toString().trim())) {
                Toast.makeText(this, "Your new password should be different from the old one", Toast.LENGTH_SHORT).show();
                return;
            }
            String regExp = "^(?![0-9]+$)(?![^0-9]+$)(?![a-zA-Z]+$)(?![^a-zA-Z]+$)(?![a-zA-Z0-9]+$)[a-zA-Z0-9\\S]{8,}$";

            if(temp1.length() < 8){
                Toast.makeText(this, "The length of password should be more than 8", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!temp1.matches(regExp)) {
                Toast.makeText(this, "Password should start with [A-Za-z] and bound with [0-9] and special symbol", Toast.LENGTH_SHORT).show();
                return;
            }
            isSuccess = user.updateInfo(id, type, temp1);
        }else if(type.equals("email")){
            String temp = email2.getText().toString().trim();
            if (temp.isEmpty()){
                Toast.makeText(this,"The email address should not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (temp.equals(email1.getText().toString().trim())) {
                Toast.makeText(this, "Your new email address should be different from the old one", Toast.LENGTH_SHORT).show();
                return;
            }
            String check =  "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(temp);
            if (!matcher.matches()) {
                Toast.makeText(this, "The format of your email is wrong",Toast.LENGTH_SHORT).show();
                return;
            }
            if (user.isInfoExist("email", temp)){
                Toast.makeText(this,"This email address has been used",Toast.LENGTH_SHORT).show();
                return;
            }
            isSuccess = user.updateInfo(id, type, temp);
            if(isSuccess){
                MainActivity.setEmail(temp);
            }
        }else if(type.equals("phone")){
            String temp = phone2.getText().toString().trim();
            if (temp.isEmpty()){
                Toast.makeText(this,"The phone number should not be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (temp.equals(phone1.getText().toString().trim())) {
                Toast.makeText(this, "Your new phone number should be different from the old one", Toast.LENGTH_SHORT).show();
                return;
            }
            if (user.isInfoExist("phone", temp)){
                Toast.makeText(this,"This phone number has been used",Toast.LENGTH_SHORT).show();
                return;
            }
            isSuccess = user.updateInfo(id, type, temp);
        }else if(type.equals("sex")){
            String temp = ((RadioButton)findViewById(sex.getCheckedRadioButtonId())).getText().toString();
            isSuccess = user.updateInfo(id, type, temp);
        }else if(type.equals("birthday")){
            String temp = birthday2.getText().toString().trim();
            if(temp.isEmpty()){
                Toast.makeText(this,"Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }
            if(temp.equals(birthday1.getText().toString().trim())){
                Toast.makeText(this,"The data you select is the same as before", Toast.LENGTH_SHORT);
                return;
            }
            isSuccess = user.updateInfo(id, type, temp);
        }
        if(isSuccess){
            //call the broadcast receiver in the Details fragment
            Intent intent = new Intent(InfoFragment.ACTION_TAG);
            LocalBroadcastManager.getInstance(UpdateInfoActivity.this).sendBroadcast(intent);
            this.setResult(RESULT_OK, new Intent(this, MainActivity.class));
            this.finish();
        }else{
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.setResult(RESULT_CANCELED, new Intent(UpdateInfoActivity.this, MainActivity.class));
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
