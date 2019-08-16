package com.example.bhsscheduletracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ScheduleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String location;
    String week;
    String day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        Spinner locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.schedules, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(this);
        int spinnerPosition = adapter.getPosition(HomeActivity.typeOfStudent);
        locationSpinner.setSelection(spinnerPosition);

        Spinner weekSpinner = (Spinner) findViewById(R.id.weekSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.week, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSpinner.setAdapter(adapter2);
        weekSpinner.setOnItemSelectedListener(this);
        String weekFormatted;
        if (HomeActivity.currentWeek.equals("red"))
        {
            weekFormatted = "Red Week";
        }
        else
        {
            weekFormatted = "Blue Week";
        }
        int spinnerPosition2 = adapter2.getPosition(weekFormatted);
        weekSpinner.setSelection(spinnerPosition2);

        Spinner daySpinner = (Spinner) findViewById(R.id.daySpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter3);
        daySpinner.setOnItemSelectedListener(this);

        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        int spinnerPosition3 = adapter3.getPosition(formatter.format(currentDate));
        daySpinner.setSelection(spinnerPosition3);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Full Schedule");


        Button getScheduleButton = (Button) findViewById(R.id.getScheduleButton);
        getScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleActivity.this, ScheduleDisplayActivity.class);
                intent.putExtra("location",location);
                intent.putExtra("week",week);
                intent.putExtra("day",day);
                startActivity(intent);

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            Intent intent = new Intent(ScheduleActivity.this, HomeActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.fullSchedule:
                            break;
                        case R.id.map:
                            Intent intent3 = new Intent(ScheduleActivity.this, MapActivity.class);
                            startActivity(intent3);
                            break;
                    }
                    return true;
                }
            };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId())
        {
            case R.id.locationSpinner:
                location = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.weekSpinner:
                week = adapterView.getItemAtPosition(i).toString();
                break;
            case R.id.daySpinner:
                day = adapterView.getItemAtPosition(i).toString();
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
