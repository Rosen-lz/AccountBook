package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

import com.example.accountbook.service.UserService;

import java.util.Calendar;

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
        sex=(RadioGroup) findViewById(R.id.sex);
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

                uService.register(user);
                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void selectDate(View v){
        Calendar calendar= Calendar.getInstance();
        new DatePickerDialog( this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String text = "You have selected：" + year + "/" + (month + 1) + "/" + dayOfMonth;
                Toast.makeText( RegisterActivity.this, text, Toast.LENGTH_SHORT ).show();
                birthday.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
            }
        }
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
