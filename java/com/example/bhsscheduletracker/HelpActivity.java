package com.example.bhsscheduletracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Help");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String helpText = "Click on the food icon on the home screen to personalize your lunches.\n\nClick on the drop down" +
                " slider on the home screen to change the type of student you are.\n\nThe button that tooggles between CHANGE TO BLUE WEEK and CHANGE TO RED WEEK" +
                " are for you to change which week it is each week.\n\nOn the bottom of the screen, you can click on \"Full Schedule\"" +
                " to view the full schedule for any day.\n\nClick on \"Maps\" to view a map of BHS.";
        TextView helpTextView = (TextView) findViewById(R.id.helpTextView);
        helpTextView.setText(helpText);
    }
}
