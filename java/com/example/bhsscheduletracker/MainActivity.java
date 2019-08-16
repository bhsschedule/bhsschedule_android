package com.example.bhsscheduletracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent1);
                finish();
            }
        },1000);


//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
//        Menu menu = bottomNavigationView.getMenu();
//        MenuItem menuItem = menu.getItem(1);
//        menuItem.setChecked(true);


    }
//
//    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    switch (item.getItemId())
//                    {
//                        case R.id.home:
//                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                            break;
//                        case R.id.fullSchedule:
//                            Intent intent2 = new Intent(MainActivity.this, ScheduleActivity.class);
//                            startActivity(intent2);
//                            break;
//                        case R.id.map:
//                            Intent intent3 = new Intent(MainActivity.this, MapActivity.class);
//                            startActivity(intent3);
//                            break;
//                    }
//                    return true;
//                }
//            };
//
}