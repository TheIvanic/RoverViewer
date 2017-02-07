package com.example.owner.rover_viewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class DateSelect extends AppCompatActivity {

    String rover_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_select);

        EditText year = (EditText) findViewById(R.id.yearInput);
        year.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "2017")});

        EditText month = (EditText) findViewById(R.id.monthInput);
        month.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "12")});

        EditText day = (EditText) findViewById(R.id.dayInput);
        day.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "31")});
        
        Intent intent = getIntent();
        if(intent !=null) {
            rover_name = intent.getExtras().getString("roverID");
                }
    }

    public void pickEarthDate(View view) {

        Intent earthDateIntent = new Intent(this, Albums.class);
        earthDateIntent.putExtra("roverID",rover_name);

        EditText yearText = (EditText) findViewById(R.id.yearInput);
        String year_value = yearText.getText().toString();
        earthDateIntent.putExtra("year",year_value);

        EditText monthText = (EditText) findViewById(R.id.monthInput);
        String month_value = monthText.getText().toString();
        earthDateIntent.putExtra("month",month_value);

        EditText dayText = (EditText) findViewById(R.id.dayInput);
        String day_value = dayText.getText().toString();
        earthDateIntent.putExtra("day",day_value);

        startActivity(earthDateIntent);

    }

    public void pickSolDate(View view) {

        Intent solDateIntent = new Intent(this, Albums.class);
        solDateIntent.putExtra("roverID",rover_name);

        EditText solText = (EditText) findViewById(R.id.solInput);
        String sol_value = solText.getText().toString();
        solDateIntent.putExtra("sol",sol_value);

        startActivity(solDateIntent);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
