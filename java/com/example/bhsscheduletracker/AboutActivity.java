package com.example.bhsscheduletracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String aboutText = "The Brookline High School Schedule Tracker app (available on both Android and iOS) was created" +
                " by Yuen Ler Chow '21 during the summer of 2019. He was motivated to create this app " +
                "after learning about the intricacies of the high school's schedules for the 2019-2020 " +
                "and 2020-2021 school years. He hopes that the app - which has features including a countdown" +
                " to the end of blocks for all three schedules (115 Greenough, Begin @ OLS, Begin @ 115)" +
                " and both Red and Blue week, a lunch personalization option, and a map of the school - " +
                "will reduce the confusion for students and faculty alike. \n\n" +
                "Acknowledgements: \n" +
                "- Math Teacher Marika Alibhai for first suggesting the creation of such an app \n" +
                "- Alumna Yuen Ting Chow '19 for designing the splash screen and contributing to app layout \n\n" +
                "" +
                "The app's source code is published at bhsschedule.weebly.com to foster interest in computer science for the future" +
                " generation of young coders. For updates on the app, please follow us on " +
                "Instagram @bhsschedule. Please direct any questions, suggestions, or complaints of app malfunction to" +
                " bhsscheduletracker@gmail.com."
                ;
        TextView aboutTextView = (TextView) findViewById(R.id.aboutTextView);
        aboutTextView.setText(aboutText);

    }
}
