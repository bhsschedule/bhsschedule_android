package com.example.bhsscheduletracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ScheduleDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Full Schedule");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        String location = getIntent().getStringExtra("location");
        String week = getIntent().getStringExtra("week");
        String day = getIntent().getStringExtra("day");
        ArrayList<String>dayNames = new ArrayList<String >();
        dayNames.add("Monday");
        dayNames.add("Tuesday");
        dayNames.add("Wednesday");
        dayNames.add("Thursday");
        dayNames.add("Friday");

        String schedule = "";

        int dayNum = dayNames.indexOf(day);

        if (location.equals("115 Greenough")){
            if (week.equals("Red Week")){
                schedule = scheduleRed(location, week, dayNum);
            }
            else if (week.equals("Blue Week")){
                schedule = scheduleBlue(location, week, dayNum);
            }
        }
        else if (location.equals("Begin @ 115")){
            if (week.equals("Red Week")){
                schedule = schedule115Red(location, week, dayNum);
            }
            else if (week.equals("Blue Week")){
                schedule = schedule115Blue(location, week, dayNum);
            }

        }
        else if (location.equals("Begin @ OLS")){
            if (week.equals("Red Week")){
                schedule = scheduleOLSRed(location, week, dayNum);
            }
            else if (week.equals("Blue Week")){
                schedule = scheduleOLSBlue(location, week, dayNum);
            }
        }

        TextView fullScheduleTextView = (TextView) findViewById(R.id.fullScheduleTextView);
        fullScheduleTextView.setText(schedule);
    }




    // THIS IS THE RED SCHEDULE FOR 115 GREENOUGH
    public String scheduleRed(String location, String week, int day)
    {
        int[][] timeStart = {{730, 820, 925, 1030, 1105, 1240, 1345},
                {730, 820, 930, 1040, 1220, 1330},
                {730, 820, 920, 1020, 1120, 1250, 1350},
                {730, 820, 930, 1040, 1115, 1200, 1340},
                {730, 830, 940, 1045, 1155, 1335}};

        int[][] timeEnd = {{815, 920, 1025, 1100, 1235, 1340, 1450},
                {815, 925, 1035, 1215, 1325, 1435},
                {815, 915, 1015, 1115, 1245, 1345, 1445},
                {815, 925, 1035, 1115, 1155, 1335, 1445},
                {825, 935, 1040, 1150, 1330, 1440}};


        String[][] blocks = {{"Z", "A", "B", "T", "D", "E", "G"},
                {"Z", "C", "E", "D", "F", "G"},
                {"Z", "A", "B", "C", "E", "D", "G"},
                {"Z", "B", "A", "T", "X", "G", "F"},
                {"Z", "B", "C", "E", "D", "F"}};

        String[] lunchBlocks = {"D","D","E","G","D"};

        return schedule(timeStart, timeEnd, blocks, lunchBlocks, day, location, week);

    }


    // THIS IS THE BLUE SCHEDULE FOR 115 GREENOUGH
    public String scheduleBlue(String location, String week, int day)
    {
        int[][] timeStart = {{730, 820, 930, 1005, 1110, 1250, 1350},
                {730, 820, 930, 1040, 1220, 1330},
                {730, 820, 925, 1030, 1105, 1240, 1345},
                {810, 940, 1050, 1200, 1345},
                {730, 830, 940, 1050, 1200, 1340}};

        int[][] timeEnd = {{815, 925, 1000, 1105, 1245, 1345, 1450},
                {815, 925, 1035, 1215, 1325, 1430},
                {815, 920, 1025, 1100, 1235, 1340, 1445},
                {930, 1045, 1155, 1340, 1450},
                {825, 935, 1045, 1155, 1335, 1440}};

        String[][] blocks = {{"Z", "A", "T", "C", "E", "F", "G"},
                {"Z", "A", "B", "C", "D", "F"},
                {"Z", "A", "B", "X", "E", "F", "G"},
                {"Faculty Collaboration\n", "C", "D", "F", "G"},
                {"Z", "A", "B", "C", "D", "E"}};

        String[] lunchBlocks = {"E","C","E","F","D"};

        return schedule(timeStart, timeEnd, blocks, lunchBlocks, day, location, week);

    }


    //SCHEDULE FOR STARTING AT 115 AND GOING TO OLS. SCHEDULE NOT COMPLETED
    public String schedule115Red(String location, String week, int day){

        int[][] timeStart = {{730, 820, 925, 1045, 1119, 1257, 1400},
                {820, 924, 958, 1102, 1240, 1345},
                {730, 820, 920, 1035, 1135, 1305, 1405},
                {730, 820, 930, 1040, 1115, 1230, 1340},
                {730, 830, 955, 1100, 1210, 1355}};

        int[][] timeEnd = {{815, 920, 1025, 1115, 1253, 1357, 1500},
                {920, 954, 1058, 1236, 1340, 1450},
                {815, 915, 1015, 1130, 1300, 1400, 1500},
                {815, 925, 1035, 1110, 1155, 1335, 1445},
                {825, 935, 1055, 1205, 1350, 1500}};

        String[][] blocks = {{"Z", "A", "B", "T", "D", "E", "G"},
                {"C", "T/H", "E", "D", "F", "G"},
                {"Z", "A", "B", "C", "E", "D", "G"},
                {"Z", "B", "A", "Lunch", "X", "G", "F"},
                {"Z", "B", "C", "E", "D", "F"}};

        String[] lunchBlocks = {"D","D","E","Lunch@115","D"};

        return schedule(timeStart, timeEnd, blocks, lunchBlocks, day, location, week);
    }


    public String schedule115Blue(String location, String week, int day){

        int[][] timeStart = {{730, 820, 945, 1048, 1122, 1300, 1400},
                {730, 820, 930, 1055, 1205, 1345},
                {730, 820, 925, 1045, 1119, 1257, 1356},
                {810, 935, 1050, 1200, 1350},
                {730, 830, 940, 1105, 1215, 1355}};

        int[][] timeEnd = {{815, 925, 1045, 1118, 1257, 1355, 1500},
                {815, 925, 1035, 1200, 1340, 1450},
                {815, 920, 1025, 1115, 1253, 1352, 1500},
                {930, 1045, 1155, 1345, 1455},
                {825, 935, 1045, 1210, 1350, 1500}};

        String[][] blocks = {{"Z", "A", "C", "T", "E", "F", "G"},
                {"Z", "A", "B", "C", "D", "F"},
                {"Z", "A", "B", "T/H", "E", "F", "G"},
                {"Faculty Collaboration\n", "C", "D", "F", "G"},
                {"Z", "A", "B", "C", "D", "E"}};

        String[] lunchBlocks = {"E","D","E","F","D"};

        return schedule(timeStart, timeEnd, blocks, lunchBlocks, day, location, week);
    }



    public String scheduleOLSRed(String location, String week, int day){
        int[][] timeStart = {{800, 904, 1008, 1047, 1225, 1345},
                {800, 915, 1025, 1220, 1330},
                {800, 859, 1003, 1137, 1236, 1350},
                {825, 940, 1115, 1200, 1340},
                {800, 859, 1003, 1037, 1215, 1335}};

        int[][] timeEnd = {{900, 1004, 1043, 1221, 1325, 1450},
                {910, 1020, 1200, 1325, 1435},
                {855, 959, 1133, 1232, 1331, 1445},
                {935, 1050, 1155, 1335, 1445},
                {855, 959, 1033, 1211, 1315, 1440}};

        String[][] blocks = {{"A", "B", "T", "D", "E", "G"},
                {"C", "E", "D", "F", "G"},
                {"A", "B", "C", "E", "D", "G"},
                {"B", "A","X", "G", "F"},
                {"B", "C","T/H", "E", "D", "F"}};

        String[] lunchBlocks = {"D","D","C","G","E"};

        return schedule(timeStart, timeEnd, blocks, lunchBlocks, day, location, week);
    }


    public String scheduleOLSBlue(String location, String week, int day){

        int[][] timeStart = {{800, 904, 938, 1047, 1250, 1350},
                {800, 910, 1020, 1205, 1330},
                {800, 905, 1010, 1045, 1240, 1345},
                {810, 935, 1040, 1200, 1345},
                {815, 925, 1035, 1215, 1330}};

        int[][] timeEnd = {{900, 934, 1043, 1225, 1345, 1450},
                {905, 1015, 1200, 1310, 1430},
                {900, 1005, 1040, 1220, 1340, 1445},
                {930, 1035, 1140, 1340, 1450},
                {920, 1030, 1210, 1325, 1440}};

        String[][] blocks = {{"A", "T", "C", "E", "F", "G"},
                {"B", "A", "C", "D", "F"},
                {"A","B", "T/H", "E", "F", "G"},
                {"Faculty Collaboration\n", "C", "D", "F", "G"},
                {"A", "B", "C", "D", "E"}};

        String[] lunchBlocks = {"E","C","E","F","C"};

        return schedule(timeStart, timeEnd, blocks, lunchBlocks, day, location, week);
    }




    public String schedule(int[][] timeStart, int[][] timeEnd, String[][] block, String[] lunch, int day, String location, String week){
        String schedule = "";
        for (int i = 0; i < block[day].length; i++){
            schedule += block[day][i];
            schedule += "    ";
            schedule += formatTime(timeStart[day][i]);
            schedule += " - ";
            schedule += formatTime(timeEnd[day][i]);
            schedule += "\n";
            if (block[day][i].equals(lunch[day])){
                schedule += handleLunch(location, week, day);
            }
            schedule += "\n";
        }
        return schedule;
    }


    public String formatTime(int time)
    {
        String formatted = "";
        if (time >= 1300){
            formatted = Integer.toString(time-1200);
        }
        else{
            formatted = Integer.toString(time);
        }
        return formatted.substring(0,formatted.length()-2) + ":" + formatted.substring(formatted.length()-2);
    }


    public String handleLunch(String location, String week, int day)
    {

        String schedule = "";

        String[][] red = {{"11:05 - 11:35","10:40 - 11:10","11:15 - 11:45","12:30 - 1:00","12:30 - 1:00"},
                        {"12:05 - 12:35","11:45 - 12:15","12:15 - 12:45","11:55 - 12:25","11:55 - 12:25"}};

        String[][] blue = {{"11:10 - 11:40","10:35 - 11:05","11:00 - 11:30","12:30 - 1:00","12:30- 1:00"},
                {"12:15 - 12:45","11:45 - 12:15","12:05 - 12:45","12:00 - 12:30","11:55 - 12:25"}};

        String[][] red115 = {{"11:50 - 12:20","11:36 - 12:06","12:05 - 12:35","10:40 - 11:10","12:40- 1:10"},
                {"12:23 - 12:53","12:06 - 12:36","11:35 - 12:05","10:40 - 11:10","12:10 - 12:40"}};


        String[][] blue115 = {{"11:52 - 12:22","12:35 - 1:05","11:49 - 12:19","12:30 - 1:00","12:45- 1:15"},
                {"12:27 - 12:57","12:05 - 12:35","12:24 - 12:54","12:00 - 12:30","12:15 - 12:45"}};

        String[][] redOLS = {{"11:17 - 11:47","10:55 - 11:25","10:33 - 11:03","12:30 - 1:00","11:07- 11:37"},
                {"10:47 - 11:17","10:25 - 10:55","11:03 - 11:33","11:55 - 12:25","11:42 - 12:12"}};

        String[][] blueOLS = {{"11:17 - 11:47","10:50 - 11:20","11:15 - 11:45","12:30 - 1:00","11:05- 11:35"},
                {"10:47 - 11:17","11:30 - 12:00","10:45 - 11:15","11:55 - 12:25","11:40 - 12:10"}};





        if (location.equals("115 Greenough"))
        {
            if (week.equals("Red Week"))
            {
                schedule += "   Lunch A: " + red[0][day];
                schedule += "\n";
                schedule += "   Lunch B: " + red[1][day];

            }
            else if (week.equals("Blue Week"))
            {
                schedule += "   Lunch A: " + blue[0][day];
                schedule += "\n";
                schedule += "   Lunch B: " + blue[1][day];
            }
        }
        else if (location.equals("Begin @ 115"))
        {
            if (week.equals("Red Week"))
            {
                schedule += "   Lunch A: " + red115[0][day];
                schedule += "\n";
                schedule += "   Lunch B: " + red115[1][day];
            }
            else if (week.equals("Blue Week"))
            {
                schedule += "   Lunch A: " + blue115[0][day];
                schedule += "\n";
                schedule += "   Lunch B: " + blue115[1][day];
            }
        }


        else if (location.equals("Begin @ OLS"))
        {
            if (week.equals("Red Week"))
            {
                schedule += "   Lunch A: " + redOLS[0][day];
                schedule += "\n";
                schedule += "   Lunch B: " + redOLS[1][day];
            }
            else if (week.equals("Blue Week"))
            {
                schedule += "   Lunch A: " + blueOLS[0][day];
                schedule += "\n";
                schedule += "   Lunch B: " + blueOLS[1][day];
            }
        }
        schedule += "\n";
        return schedule;
    }






    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            Intent intent = new Intent(ScheduleDisplayActivity.this, HomeActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.fullSchedule:
                            Intent intent2 = new Intent(ScheduleDisplayActivity.this, ScheduleActivity.class);
                            startActivity(intent2);
                            break;
                        case R.id.map:
                            Intent intent3 = new Intent(ScheduleDisplayActivity.this, MapActivity.class);
                            startActivity(intent3);
                            break;
                    }
                    return true;
                }
            };
}
