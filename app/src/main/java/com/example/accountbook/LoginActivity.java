package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
        //hide the option menu to forbid the paste function
        username.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        password.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);

        // connect the SQLite
        SQLiteStudioService.instance().start(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=username.getText().toString();
                System.out.println(name);
                String pass=password.getText().toString();
                System.out.println(pass);
                if (name.equals("") || password.equals("")){
                    Toast.makeText((LoginActivity.this), "Please enter your account information", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.i("TAG",name+"_"+pass);
                UserService uService=new UserService(LoginActivity.this);
                boolean flag = uService.login(name, pass);

                if(flag){
                    Log.i("TAG","Login successfully");
                    Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", name);
                    startActivity(intent);
                }else{
                    Log.i("TAG","Fail to login");
                    Toast.makeText(LoginActivity.this, "Fail to login", Toast.LENGTH_LONG).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

}
