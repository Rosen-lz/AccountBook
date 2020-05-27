package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accountbook.model.User;
import com.example.accountbook.service.UserService;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    TextView birthday;
    RadioGroup sex;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=(EditText) findViewById(R.id.register_userName);
        password=(EditText) findViewById(R.id.register_password);
        birthday=(TextView) findViewById(R.id.register_birthday);
        sex=(RadioGroup) findViewById(R.id.add_type);
        register=(Button) findViewById(R.id.button_register2);

        //connect to the SQLite
        SQLiteStudioService.instance().start(this);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name=username.getText().toString().trim();
                String pass=password.getText().toString().trim();
                String birthdayStr=birthday.getText().toString().trim();
                String sexStr=((RadioButton)RegisterActivity.this.findViewById(sex.getCheckedRadioButtonId())).getText().toString();

                Log.i("TAG",name+"_"+pass+"_"+birthdayStr+"_"+sexStr);

                UserService uService = new UserService(RegisterActivity.this);

                User user = new User();
                user.setUsername(name);
                user.setPassword(pass);
                user.setBirthday(birthdayStr);
                user.setSex(sexStr);

                if(user.getUsername().equals("") || user.getPassword().equals("")){
                    Toast.makeText(RegisterActivity.this, "Please enter data", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.length() > 10){
                    Toast.makeText(RegisterActivity.this, "The length of username should be less than 10", Toast.LENGTH_SHORT).show();
                    return;
                }
                String regExp = "^[A-Za-z][\\w_]{5,10}$";
                if(!name.matches(regExp)){
                    Toast.makeText(RegisterActivity.this, "The format of the username is illegal: [A-Za-z]+[\\w]", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.length() < 8){
                    Toast.makeText(RegisterActivity.this, "The length of password should be more than 8", Toast.LENGTH_SHORT).show();
                    return;
                }
                String regExp2 = "^(?![0-9]+$)(?![^0-9]+$)(?![a-zA-Z]+$)(?![^a-zA-Z]+$)(?![a-zA-Z0-9]+$)[a-zA-Z0-9\\S]{8,}$";
                if(!pass.matches(regExp2)) {
                    Toast.makeText(RegisterActivity.this, "Password should start with [A-Za-z] and bound with [0-9] and special symbol", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(birthdayStr.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please select your birthday", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(uService.register(user)){
                    Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.putExtra("username_return",name);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, "This username has been used", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void selectDate(View v){
        Calendar calendar= Calendar.getInstance();
        new DatePickerDialog( this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String text = "You have selected：" + year + "-" + (month + 1) + "-" + dayOfMonth;
                Toast.makeText( RegisterActivity.this, text, Toast.LENGTH_SHORT ).show();
                birthday.setText(year + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", dayOfMonth));
            }
        }
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
