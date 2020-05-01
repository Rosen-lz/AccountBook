package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accountbook.service.UserService;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText) findViewById(R.id.login_userName);
        password=(EditText) findViewById(R.id.login_password);
        login=(Button) findViewById(R.id.button_login);
        register=(TextView) findViewById(R.id.button_register1);

        // connect the SQLite
        SQLiteStudioService.instance().start(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=username.getText().toString();
                System.out.println(name);
                String pass=password.getText().toString();
                System.out.println(pass);

                Log.i("TAG",name+"_"+pass);
                UserService uService=new UserService(LoginActivity.this);
                boolean flag = uService.login(name, pass);

                if(flag){
                    Log.i("TAG","Login successfully");
                    Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_LONG).show();
                    //Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                    //startActivity(intent);
                }else{
                    Log.i("TAG","Fail to login");
                    Toast.makeText(LoginActivity.this, "Fail to login", Toast.LENGTH_LONG).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
