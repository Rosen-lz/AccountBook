package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayFlowInfo extends AppCompatActivity {
    TextView textView_type, textView_date, textView_note, textView_category, textView_money, textView_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_flow_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView_category = findViewById(R.id.display_category);
        textView_date = findViewById(R.id.display_date);
        textView_money = findViewById(R.id.display_money);
        textView_type = findViewById(R.id.display_type);
        textView_note = findViewById(R.id.display_note);
        textView_location = findViewById(R.id.display_location);

        Intent intent = getIntent();
        textView_type.setText(intent.getStringExtra("type"));
        textView_money.setText(intent.getStringExtra("money"));
        textView_date.setText(intent.getStringExtra("date"));
        textView_category.setText(intent.getStringExtra("category"));
        textView_note.setText(intent.getStringExtra("note"));
        textView_location.setText(intent.getStringExtra("location"));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.setResult(RESULT_OK, new Intent(this, MainActivity.class));
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
