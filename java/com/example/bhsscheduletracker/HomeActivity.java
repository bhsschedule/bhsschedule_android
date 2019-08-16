package com.example.bhsscheduletracker;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //INITIALIZING VARIABLES
    public static String currentWeek;
    public static String typeOfStudent;
    public static Map<String,String> lunchesHome = new HashMap<String,String>(){{
        put("A","No Lunch");
        put("B","No Lunch");
        put("C","No Lunch");
        put("D","No Lunch");
        put("E","No Lunch");
        put("F","No Lunch");
        put("G","No Lunch");
    }};

    //INITIALIZING VARIABLES FOR SAVING DATA
    static SharedPreferences sharedPreferences;
    static final String myPreference = "myPref";
    static final String week = "weekKey";
    static final String student = "studentKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        retrieveData();


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.schedules, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        int spinnerPosition = adapter.getPosition(typeOfStudent);
        spinner.setSelection(spinnerPosition);

        //UPDATES APP TO THE STATE OF LAST OPEN
        reload();


//      THIS IS THE CODE FOR THE BUTTON THAT CHANGES TO RED AND BLUE WEEK
        final Button changeWeekButton = (Button) findViewById(R.id.changeWeekButton);
        changeWeekButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TextView currentBlockTextView = (TextView) findViewById(R.id.currentBlockTextView);
                TextView nextBlockTextView = (TextView) findViewById(R.id.nextBlockTextView);

                SpannableString[] current = new SpannableString[2];
                SpannableString[] next = new SpannableString[2];
                if (currentWeek.equals("red")) {
                    currentWeek = "blue";
                    changeToBlue();
                    if (typeOfStudent.equals("115 Greenough")) {
                        current = scheduleBlue();
                        next = scheduleBlue();
                    } else if (typeOfStudent.equals("Begin @ 115")) {
                        current = schedule115Blue();
                        next = schedule115Blue();

                    } else if (typeOfStudent.equals("Begin @ OLS")) {
                        current = scheduleOLSBlue();
                        next = scheduleOLSBlue();
                    }
                    changeWeekButton.setText("Change to Red Week");
                    saveData();

                } else {
                    currentWeek = "red";
                    changeToRed();
                    if (typeOfStudent.equals("115 Greenough")) {
                        current = scheduleRed();
                        next = scheduleRed();

                    } else if (typeOfStudent.equals("Begin @ 115")) {
                        current = schedule115Red();
                        next = schedule115Red();

                    } else if (typeOfStudent.equals("Begin @ OLS")) {
                        current = scheduleOLSRed();
                        next = scheduleOLSRed();

                    }
                    changeWeekButton.setText("Change to Blue Week");
                    saveData();
                }
                currentBlockTextView.setText(current[0]);
                nextBlockTextView.setText(next[1]);
            }
        });

        ImageButton lunchImageButton = (ImageButton) findViewById(R.id.lunchImageButton);
        lunchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,LunchActivity.class);
                startActivity(intent);
            }
        });

        thread.start();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            break;
                        case R.id.fullSchedule:
                            Intent intent2 = new Intent(HomeActivity.this, ScheduleActivity.class);
                            startActivity(intent2);
                            break;
                        case R.id.map:
                            Intent intent3 = new Intent(HomeActivity.this, MapActivity.class);
                            startActivity(intent3);
                            break;
                    }
                    return true;
                }
            };



    //THE MENU BUTTON ON THE TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {

            case R.id.help:
                help();
                return true;
            case R.id.about:
                about();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }




    Thread thread = new Thread() {
        public void run(){
            try{
                while (!thread.isInterrupted())
                {
                    Thread.sleep(100);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reload();
                        }
                    });
                }
            }
            catch (InterruptedException e)
            {
            }
        }
    };


    //RELOAD BUTTON: just calls everything again to update.
    public void reload() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int heightCurrentNext = height * 3 /16;


        TextView currentBlockTextView = (TextView) findViewById(R.id.currentBlockTextView);
        TextView nextBlockTextView = (TextView) findViewById(R.id.nextBlockTextView);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) currentBlockTextView.getLayoutParams();
        ConstraintLayout.LayoutParams ting = (ConstraintLayout.LayoutParams) nextBlockTextView.getLayoutParams();
        lp.height = heightCurrentNext;
        ting.height = heightCurrentNext;
        currentBlockTextView.setLayoutParams(lp);
        nextBlockTextView.setLayoutParams(ting);

        Button changeWeekButton = (Button) findViewById(R.id.changeWeekButton);
        SpannableString[] current = new SpannableString[2];
        SpannableString[] next = new SpannableString[2];
        if (currentWeek.equals("red")) {
            changeToRed();
            if (typeOfStudent.equals("115 Greenough")) {
                current = scheduleRed();
                next = scheduleRed();
            } else if (typeOfStudent.equals("Begin @ 115")) {
                current = schedule115Red();
                next = schedule115Red();
            } else if (typeOfStudent.equals("Begin @ OLS")) {
                current = scheduleOLSRed();
                next = scheduleOLSRed();

            }
            changeWeekButton.setText("Change to Blue Week");
        } else {
            changeToBlue();
            if (typeOfStudent.equals("115 Greenough")) {
                current = scheduleBlue();
                next = scheduleBlue();
            } else if (typeOfStudent.equals("Begin @ 115")) {
                current = schedule115Blue();
                next = schedule115Blue();
            } else if (typeOfStudent.equals("Begin @ OLS")) {
                current = scheduleOLSBlue();
                next = scheduleOLSBlue();

            }
            changeWeekButton.setText("Change to Red Week");
        }
        currentBlockTextView.setText(current[0]);
        nextBlockTextView.setText(next[1]);
    }


    ///     GET AND PRINT DATE
    public void printDate() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMMM dd ',' yyyy '\n' hh:mm:ss a zzz");
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setText(formatter.format(currentDate));

    }



    public void changeToRed()
    {
        TextView currentBlockTextView = (TextView) findViewById(R.id.currentBlockTextView);
        currentBlockTextView.setBackgroundColor(Color.parseColor("#C42B47"));

        TextView nextBlockTextView = (TextView) findViewById(R.id.nextBlockTextView);
        nextBlockTextView.setBackgroundColor(Color.parseColor("#FF76E4"));

        TextView dateTextView = (TextView) findViewById((R.id.dateTextView));
        dateTextView.setBackgroundColor(Color.parseColor("#AA0055"));
    }


    public void changeToBlue()
    {
        TextView currentBlockTextView = (TextView) findViewById(R.id.currentBlockTextView);
        currentBlockTextView.setBackgroundColor(Color.parseColor("#4169E1"));

        TextView nextBlockTextView = (TextView) findViewById(R.id.nextBlockTextView);
        nextBlockTextView.setBackgroundColor(Color.parseColor("#87CEFA"));

        TextView dateTextView = (TextView) findViewById((R.id.dateTextView));
        dateTextView.setBackgroundColor(Color.parseColor("#15478C"));
    }




    // THIS IS THE RED SCHEDULE FOR 115 GREENOUGH
    public SpannableString[] scheduleRed()
    {
        int[][] times = {{730, 820, 925, 1030, 1105, 1240, 1345, 1455},
                {730, 820, 930, 1040, 1220, 1330, 1440},
                {730, 820, 920, 1020, 1120, 1250, 1350, 1450},
                {730, 820, 930, 1040, 1115, 1200, 1340, 1450},
                {730, 830, 940, 1045, 1155, 1335, 1445}};

        String[][] blocks = {{"Z", "A", "B", "T", "D", "E", "G"},
                {"Z", "C", "E", "D", "F", "G"},
                {"Z", "A", "B", "C", "E", "D", "G"},
                {"Z", "B", "A", "T", "X", "G", "F"},
                {"Z", "B", "C", "E", "D", "F"}};

        String[] lunchBlocks = {"D","D","E","G","D"};

        return schedule(times, blocks, lunchBlocks);

    }


    // THIS IS THE BLUE SCHEDULE FOR 115 GREENOUGH
    public SpannableString[] scheduleBlue()
    {
        int[][] times = {{730, 820, 930, 1005, 1110, 1250, 1350, 1455},
                {730, 820, 930, 1040, 1220, 1330, 1435},
                {730, 820, 925, 1030, 1105, 1240, 1345, 1450},
                {810, 940, 1050, 1200, 1345, 1455},
                {730, 830, 940, 1050, 1200, 1340, 1445}};

        String[][] blocks = {{"Z", "A", "T", "C", "E", "F", "G"},
                {"Z", "A", "B", "C", "D", "F"},
                {"Z", "A", "B", "X", "E", "F", "G"},
                {"Faculty Collaboration", "C", "D", "F", "G"},
                {"Z", "A", "B", "C", "D", "E"}};

        String[] lunchBlocks = {"E","C","E","F","D"};

        return schedule(times, blocks, lunchBlocks);

    }


    //SCHEDULE FOR STARTING AT 115 AND GOING TO OLS. SCHEDULE NOT COMPLETED
    public SpannableString[] schedule115Red(){

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

        return scheduleFreshman(timeStart, timeEnd, blocks, lunchBlocks);
    }


    public SpannableString[] schedule115Blue(){

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
                {"Faculty Collaboration", "C", "D", "F", "G"},
                {"Z", "A", "B", "C", "D", "E"}};

        String[] lunchBlocks = {"E","D","E","F","D"};

        return scheduleFreshman(timeStart, timeEnd, blocks, lunchBlocks);
    }



    public SpannableString[] scheduleOLSRed(){
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

        return scheduleFreshman(timeStart, timeEnd, blocks, lunchBlocks);
    }


    public SpannableString[] scheduleOLSBlue(){

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
                {"Faculty Collaboration", "C", "D", "F", "G"},
                {"A", "B", "C", "D", "E"}};

        String[] lunchBlocks = {"E","C","E","F","C"};

        return scheduleFreshman(timeStart, timeEnd, blocks, lunchBlocks);
}



    // GETS CALLED BY scheduleRed() AND scheduleBlue() TO PRINT STUFF ONTO APP
    public SpannableString[] schedule ( int[][] timeArray, String[][] blockArray,  String[] lunchArray)
    {

        printDate();
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        Integer dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


        SimpleDateFormat formatterForHour = new SimpleDateFormat("HH");
        SimpleDateFormat formatterForMinute = new SimpleDateFormat("mm");
        SimpleDateFormat formatterForSecond = new SimpleDateFormat("ss");
        Integer second = Integer.valueOf(formatterForSecond.format(currentDate));
        Integer time = Integer.valueOf(formatterForHour.format(currentDate) + formatterForMinute.format(currentDate));

        SpannableString current = new SpannableString("");
        SpannableString next = new SpannableString("");
        String c;
        String n;

        if (dayOfWeek >= 2 && dayOfWeek <= 6) {

            TextView scheduleTextView = (TextView) findViewById(R.id.scheduleTextView);
            String schedule = "Today's Schedule: ";
            if (dayOfWeek == 5 && currentWeek.equals("blue"))
            {
                schedule += "C D F G";
            }
            else
            {
                for (String block : blockArray[dayOfWeek-2])
                {
                    schedule += block;
                    schedule += " ";
                }
            }
            scheduleTextView.setText(schedule);

            int i = 0;
            boolean printed = false;
            while (i < timeArray[dayOfWeek - 2].length - 1 && !printed) {
                String currentBlock = blockArray[dayOfWeek - 2][i];
                String timeUntilBlockStarts = minFormat(timeDifference(timeArray[dayOfWeek-2][i+1],time,second,2));
                String timeUntilBlockEnds = minFormat(timeDifference(minus5(timeArray[dayOfWeek-2][i+1]),time,second,2));
                if (time >= timeArray[dayOfWeek - 2][i] && time < minus5(timeArray[dayOfWeek - 2][i + 1])) {
                    if (currentBlock.equals("Faculty Collaboration")) {
                        c = ("It is now " + currentBlock + ".\nEnds in " + minFormat(timeDifference(minus5(minus5(timeArray[dayOfWeek-2][i+1])),time,second,2)) + " at 9:30.");
                        current = new SpannableString(c);
                    }
                    else {

                        if (dayOfWeek == 5 && currentBlock.equals("T"))
                        {
                            c = ("It is now " + currentBlock + " Block.\nEnds in " + timeUntilBlockEnds + " at " + formatTime(timeArray[dayOfWeek - 2][i + 1]) + ".");
                            current = new SpannableString(c);
                            current.setSpan(new RelativeSizeSpan(2f), 10, 11, 0);
                        }
                        else {
                            c = ("It is now " + currentBlock + " Block.\nEnds in " + timeUntilBlockEnds + " at " + formatTime(minus5(timeArray[dayOfWeek - 2][i + 1])) + ".");
                            if (currentBlock.equals(lunchArray[dayOfWeek - 2])) {
                                c += getLunchTime(dayOfWeek - 2, currentBlock);
                            }
                            current = new SpannableString(c);
                            current.setSpan(new RelativeSizeSpan(2f), 10, 11, 0);
                        }
                    }
                    printed = true;

                    if (i < blockArray[dayOfWeek - 2].length - 1) {
                        String nextBlock = blockArray[dayOfWeek - 2][i + 1];
                        n = ("Next is " + nextBlock + " Block.\nStarts in " + timeUntilBlockStarts + " at " + formatTime(timeArray[dayOfWeek-2][i+1]) + ".");
                        if (nextBlock.equals(lunchArray[dayOfWeek-2])){
                            n += getLunchTime(dayOfWeek-2, nextBlock);
                        }
                        next = new SpannableString(n);
                        next.setSpan(new RelativeSizeSpan(2f), 8, 9, 0);

                    } else {
                        n = ("School ends in " + timeUntilBlockEnds + ".");
                        next = new SpannableString(n);
                    }

                } else if (time >= minus5(timeArray[dayOfWeek - 2][i+1]) && time < timeArray[dayOfWeek - 2][i+1]) {
                    if (i == blockArray[dayOfWeek-2].length-1)
                    {
                        printed = false;
                    }
                    else {
                        c = ("Travel Time: " + blockArray[dayOfWeek - 2][i+1] + " Block\nStarts in " + timeUntilBlockStarts + " at " + formatTime(timeArray[dayOfWeek-2][i+1]) + ".");
                        n =  "";
                        next = new SpannableString(n);
                        current = new SpannableString(c);
                        current.setSpan(new RelativeSizeSpan(2f),13,14,0);
                        printed = true;

                    }

                }
                i++;
            }

            if (!printed) {
                if (time >= 1300) {
                    c = ("No School.\nSchool ended " + minFormat(timeDifference(time, minus5(timeArray[dayOfWeek - 2][timeArray[dayOfWeek - 2].length - 1]), second, 1)) + " ago.");
                    if (dayOfWeek < 6) {
                        String schoolRestart = minFormat(timeDifference(timeArray[dayOfWeek - 1][1], time, second, 2));
                        n = ("School starts in " + schoolRestart + ".");
                    } else {
                        n = "";
                    }
                }
                else
                {
                    if (dayOfWeek > 2) {
                        c = ("No School.\nSchool ended " + minFormat(timeDifference(time, minus5(timeArray[dayOfWeek - 3][timeArray[dayOfWeek - 3].length - 1]), second, 1)) + " ago.");
                        String schoolRestart = minFormat(timeDifference(timeArray[dayOfWeek - 2][1], time, second, 2));
                        n = "School starts in " + schoolRestart + ".";

                    }
                    else
                    {

                        String schoolRestart = minFormat(timeDifference(timeArray[dayOfWeek - 2][1], time, second, 2));
                        c = "School starts in " + schoolRestart + ".";
                        n = "";
                    }

                }
                current = new SpannableString(c);
                next = new SpannableString(n);
            }
        }
        else {
        c = ("No School.\n It is the weekend.");
        n = "";
        current = new SpannableString(c);
        next = new SpannableString(n);

        }

        SpannableString[] yuen_ler = {current,next};
        return yuen_ler;
    }

    //DOES THE SAME STUFF AS schedule() BUT IS FOR THE FRESHMAN SINCE THEY HAVE A DIFFERENT SCHEDULE
    public SpannableString[] scheduleFreshman ( int[][] timeStartArray, int[][] timeEndArray, String[][] blockArray, String[] lunchArray)
    {
        printDate();
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        Integer dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        SimpleDateFormat formatterForHour = new SimpleDateFormat("HH");
        SimpleDateFormat formatterForMinute = new SimpleDateFormat("mm");
        SimpleDateFormat formatterForSecond = new SimpleDateFormat("ss");
        Integer second = Integer.valueOf(formatterForSecond.format(currentDate));
        Integer time = Integer.valueOf(formatterForHour.format(currentDate) + formatterForMinute.format(currentDate));

        SpannableString current = new SpannableString("");
        SpannableString next = new SpannableString("");
        String c;
        String n;

        if (dayOfWeek >= 2 && dayOfWeek <= 6) {

            TextView scheduleTextView = (TextView) findViewById(R.id.scheduleTextView);
            String schedule = "Today's Schedule: ";
            if (dayOfWeek == 5 && currentWeek.equals("blue"))
            {
                schedule += "C D F G";
            }
            else
            {
                for (String block : blockArray[dayOfWeek-2])
                {
                    schedule += block;
                    schedule += " ";
                }
            }
            scheduleTextView.setText(schedule);

            int i = 0;
            boolean printed = false;
            while (i < timeStartArray[dayOfWeek - 2].length && !printed) {
                String currentBlock = blockArray[dayOfWeek - 2][i];
                String timeUntilBlockStarts = "";
                if (i < blockArray[dayOfWeek-2].length-1) {
                    timeUntilBlockStarts = minFormat(timeDifference(timeStartArray[dayOfWeek - 2][i + 1], time, second, 2));
                }

                //BLUE WEEK MORNING DOES NOT WORK FOR FRESHMAN
                String timeUntilBlockEnds = minFormat(timeDifference(timeEndArray[dayOfWeek-2][i],time,second,2));
                if (time >= timeStartArray[dayOfWeek - 2][i] && time < timeEndArray[dayOfWeek - 2][i]) {

                    if (currentBlock.equals("Faculty Collaboration"))
                    {
                        c = ("It is now " + currentBlock + ".\nEnds in " + timeUntilBlockEnds + " at 9:30.");
                        current = new SpannableString(c);
                    }
                    else {
                        c = ("It is now " + currentBlock + " Block.\nEnds in " + timeUntilBlockEnds + " at " + formatTime(timeEndArray[dayOfWeek-2][i]) + ".");
                        if (currentBlock.equals(lunchArray[dayOfWeek-2])){
                            c += getLunchTime(dayOfWeek-2, currentBlock);
                        }
                        if (currentBlock.equals("Lunch")) {
                            current = new SpannableString(c);
                        }
                        else
                        {
                            current = new SpannableString(c);
                            current.setSpan(new RelativeSizeSpan(2f), 10, 11, 0);
                        }
                    }
                    printed = true;

                    if (i < blockArray[dayOfWeek - 2].length - 1) {
                        String nextBlock = blockArray[dayOfWeek - 2][i + 1];
                        n = ("Next is " + nextBlock + " Block.\nStarts in " + timeUntilBlockStarts + " at " + formatTime(timeStartArray[dayOfWeek-2][i+1]) + ".");
                        if (nextBlock.equals(lunchArray[dayOfWeek-2])){
                            n += getLunchTime(dayOfWeek-2, nextBlock);
                        }
                        next = new SpannableString(n);
                        next.setSpan(new RelativeSizeSpan(2f), 8, 9, 0);


                    } else {
                        n = ("School ends in " + timeUntilBlockEnds + ".");
                        next = new SpannableString(n);
                    }

                } else{
                    if (i == blockArray[dayOfWeek-2].length-1)
                    {
                        printed = false;
                    }
                    else if (time >= timeEndArray[dayOfWeek - 2][i] && time < timeStartArray[dayOfWeek - 2][i+1]) {

                        c = ("Travel Time: " + blockArray[dayOfWeek - 2][i+1] + " Block\nStarts in " + timeUntilBlockStarts + " at " + formatTime(timeStartArray[dayOfWeek-2][i+1]) + ".");
                        n = "";
                        current = new SpannableString(c);
                        current.setSpan(new RelativeSizeSpan(2f),13,14,0);
                        next = new SpannableString(n);
                        printed = true;

                    }

                }
                i++;
            }
            if (!printed) {
                if (time >= 1300) {
                    c = ("No School.\nSchool ended " + minFormat(timeDifference(time, timeEndArray[dayOfWeek-2][timeStartArray[dayOfWeek-2].length-1],second,1)) + " ago.");
                    if (dayOfWeek < 6) {
                        if (typeOfStudent.equals("Begin @ 115") && dayOfWeek == 2 && currentWeek.equals("red")) {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 1][0], time, second, 2));
                            n = ("School starts in " + schoolRestart + ".");
                        } else if (typeOfStudent.equals("Begin @ 115")) {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 1][1], time, second, 2));
                            n = ("School starts in " + schoolRestart + ".");
                        } else if (typeOfStudent.equals("Begin @ OLS") && dayOfWeek == 4 && currentWeek.equals("blue")) {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 1][1], time, second, 2));
                            n = ("School starts in " + schoolRestart + ".");
                        } else {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 1][0], time, second, 2));
                            n = ("School starts in " + schoolRestart + ".");
                        }

                    } else {
                        n = "";
                    }
                }
                else {
                    if (dayOfWeek > 2) {
                        c = ("No School.\nSchool ended " + minFormat(timeDifference(time, timeEndArray[dayOfWeek - 3][timeStartArray[dayOfWeek - 3].length - 1], second, 1)) + " ago.");
                        if (typeOfStudent.equals("Begin @ 115") && dayOfWeek == 3 && currentWeek.equals("red")) {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 2][0], time, second, 2));
                            n = ("School starts in " + schoolRestart + ".");
                        } else if (typeOfStudent.equals("Begin @ 115")) {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 2][1], time, second, 2));
                            n = ("School starts in " + schoolRestart + ".");
                        } else if (typeOfStudent.equals("Begin @ OLS") && dayOfWeek == 5 && currentWeek.equals("blue")) {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 2][1], time, second, 2));
                            n = ("School starts in " + schoolRestart + ".");
                        } else {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 2][0], time, second, 2));
                            n = ("School starts in " + schoolRestart + ".");
                        }
                    }
                    else
                    {
                        if (typeOfStudent.equals("Begin @ 115") && dayOfWeek == 3 && currentWeek.equals("red")) {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 2][0], time, second, 2));
                            c = ("School starts in " + schoolRestart + ".");
                        } else if (typeOfStudent.equals("Begin @ 115")) {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 2][1], time, second, 2));
                            c = ("School starts in " + schoolRestart + ".");
                        } else if (typeOfStudent.equals("Begin @ OLS") && dayOfWeek == 5 && currentWeek.equals("blue")) {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 2][1], time, second, 2));
                            c = ("School starts in " + schoolRestart + ".");
                        } else {
                            String schoolRestart = minFormat(timeDifference(timeStartArray[dayOfWeek - 2][0], time, second, 2));
                            c = ("School starts in " + schoolRestart + ".");
                        }
                        n = "";
                    }


                }
                current = new SpannableString(c);
                next = new SpannableString(n);

            }
        }
        else {
            c = ("No School.\n It is the weekend.");
            n = "";
            current = new SpannableString(c);
            next = new SpannableString(n);

        }

        SpannableString[] may = {current,next};
        return may;
    }

    public String getLunchTime(int day, String block) {
        String week = currentWeek;
        String student = typeOfStudent;
        String lunch = lunchesHome.get(block);

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




        if (student.equals("115 Greenough"))
        {
            if (week.equals("red"))
            {
                if (lunch.equals("Lunch A")) {
                    return "\nLunch: " + red[0][day];
                }
                else if (lunch.equals("Lunch B")) {
                    return "\nLunch: " + red[1][day];
                }
                else
                {
                    return "";
                }
            }
            else if (week.equals("blue"))
            {
                if (lunch.equals("Lunch A")) {
                    return "\nLunch: " + blue[0][day];
                }
                else if (lunch.equals("Lunch B")) {
                    return "\nLunch: " + blue[1][day];
                }
                else
                {
                    return "";
                }
            }
        }
        else if (student.equals("Begin @ 115"))
        {
            if (week.equals("red"))
                {
                    if (lunch.equals("Lunch A")) {
                        return "\nLunch: " + red115[0][day];
                    }
                    else if (lunch.equals("Lunch B")) {
                        return "\nLunch: " + red115[1][day];
                    }
                    else
                    {
                        return "";
                    }
                }
            else if (week.equals("blue"))
                {
                    if (lunch.equals("Lunch A")) {
                        return "\nLunch: " + blue115[0][day];
                    }
                    else if (lunch.equals("Lunch B")) {
                        return "\nLunch: " + blue115[1][day];
                    }
                    else
                    {
                        return "";
                    }
                }
            }


        else if (student.equals("Begin @ OLS"))
        {
            if (week.equals("red"))
            {
                if (lunch.equals("Lunch A")) {
                    return "\nLunch: " + redOLS[0][day];
                }
                else if (lunch.equals("Lunch B")) {
                    return "\nLunch: " + redOLS[1][day];
                }
                else
                {
                    return "";
                }
            }
            else if (week.equals("blue"))
            {
                if (lunch.equals("Lunch A")) {
                    return "\nLunch: " + blueOLS[0][day];
                }
                else if (lunch.equals("Lunch B")) {
                    return "\nLunch: " + blueOLS[1][day];
                }
                else
                {
                    return "";
                }
            }
        }

        return "ERROR 1";

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



    // GIVES THE TIME DIFFERENCE BETWEEN 2 TIMES (time1 is ahead of time2). DATE DOES NOT MATTER. ASSUMES NEAREST DATE.
    public int[] timeDifference(int time1, int time2, int currentSecond, int whichOneIsNow)
    {

        if (time1 >= time2) {
            int timeDiff = 0;
            int progressTime = time2;
            while ((time1 - progressTime) > 100) {
                timeDiff += 60;
                progressTime += 100;
            }

            if (time1/100 != progressTime/100) {
                timeDiff += (time1-progressTime-40);
            }
            else {
                timeDiff += (time1-progressTime);
            }


            if (whichOneIsNow == 1) {
                int[] arr = {timeDiff, currentSecond};
                return arr;
            }
            else {
                int[] arr = {timeDiff-1, 60-currentSecond};
                return arr;
            }
        }
        else
        {
            int timeDiff = 0;
            int progressTime = time1;
            while ((time2 - progressTime) > 100) {
                timeDiff += 60;
                progressTime += 100;
            }
            if (time2/100 != progressTime/100) {
                timeDiff += (time2-progressTime-40);
            }
            else {
                timeDiff += (time2-progressTime);
            }
            if (whichOneIsNow == 1) {
                int[] arr = {(24*60)-timeDiff, currentSecond};
                return arr;
            }
            else {
                int[] arr = {(24*60)-timeDiff-1, 60-currentSecond};
                return arr;
            }
        }
    }



    public String minFormat(int[] minAndSec)
    {

        if (minAndSec[0] >= 60) {
            return minAndSec[0]/60 + " hr " + minAndSec[0]%60 + " min";
        }
        else {
            return minAndSec[0] + " min " + minAndSec[1] + " sec";
        }
    }




    //GOES BACK IN TIME 5 MINUTES. RECEIVES AND RETURNS A 3 DIGIT INT
    public int minus5(int time) {
        if ((time%100 - 5) < 0) {
            return (time - time%100 - 100) + (60 + (time%100 - 5));
        }
        else {
            return time - 5;
        }
    }





    public void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(week,currentWeek);
        editor.putString(student, typeOfStudent);
        String[] kai = {"A","B","C","D","E","F","G"};
        for (String letter : kai) {
            editor.putString(letter, lunchesHome.get(letter));
        }
        editor.commit();
    }




    public void retrieveData()
    {
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(week)) {
            currentWeek = sharedPreferences.getString(week, "");
        }
        else {
            currentWeek = "red";
        }



        if (sharedPreferences.contains(student)) {
            typeOfStudent = sharedPreferences.getString(student,"");
        }
        else {
            typeOfStudent = "115 Greenough";
        }


        String[] kai= {"A","B","C","D","E","F","G"};
        for (String letter : kai) {
            if (sharedPreferences.contains(letter)) {
                lunchesHome.put(letter, sharedPreferences.getString(letter, ""));
            }
        }

    }


    //These two methods are for the spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        typeOfStudent =  adapterView.getItemAtPosition(i).toString();
        reload();
        saveData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //THESE THREE METHODS ARE FOR THE DROPDOWN MENU



    public void help() {
        Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
        HomeActivity.this.startActivity(intent);
    }


    public void about() {
        Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
        HomeActivity.this.startActivity(intent);
    }

}

