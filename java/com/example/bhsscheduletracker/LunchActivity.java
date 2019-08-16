package com.example.bhsscheduletracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import static com.example.bhsscheduletracker.HomeActivity.lunchesHome;
import static com.example.bhsscheduletracker.HomeActivity.sharedPreferences;


public class LunchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    static final String myPreference = "myPref";
    RadioGroup lunchesRadioGroup;
    String block;
    public static Map<String,String> lunches = new HashMap<String,String>(){{
        put("A","No Lunch");
        put("B","No Lunch");
        put("C","No Lunch");
        put("D","No Lunch");
        put("E","No Lunch");
        put("F","No Lunch");
        put("G","No Lunch");
    }};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);

        updateMap();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lunch Personalization");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String instructions = "Select which lunch you have for each block.";
        TextView instructionsTextView = (TextView) findViewById(R.id.instructionsTextView);
        instructionsTextView.setText(instructions);

        Spinner spinner = (Spinner) findViewById(R.id.blockSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lunchBlocks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (lunches.get("A").equals("No Lunch"))
        {
            RadioButton noLunchRadio = (RadioButton) findViewById(R.id.noLunchRadio);
            noLunchRadio.setChecked(true);
        }
        else if (lunches.get("A").equals("Lunch A"))
        {
            RadioButton lunchARadio = (RadioButton) findViewById(R.id.lunchARadio);
            lunchARadio.setChecked(true);
        }
        else
        {
            RadioButton lunchBRadio = (RadioButton) findViewById(R.id.lunchBRadio);
            lunchBRadio.setChecked(true);
        }

        lunchesRadioGroup = (RadioGroup) findViewById(R.id.lunchesRadioGroup);
        lunchesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(i);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    lunches.put(block,checkedRadioButton.getText().toString());
                    saveMap();
                }

            }


        });
    }







    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        block =  adapterView.getItemAtPosition(i).toString();

        if (lunches.get(block).equals("No Lunch"))
        {
            RadioButton noLunchRadio = (RadioButton) findViewById(R.id.noLunchRadio);
            noLunchRadio.setChecked(true);
        }
        else if (lunches.get(block).equals("Lunch A"))
        {
            RadioButton lunchARadio = (RadioButton) findViewById(R.id.lunchARadio);
            lunchARadio.setChecked(true);
        }
        else
        {
            RadioButton lunchBRadio = (RadioButton) findViewById(R.id.lunchBRadio);
            lunchBRadio.setChecked(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void saveMap()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String[] kai = {"A","B","C","D","E","F","G"};
        for (String letter : kai) {
            editor.putString(letter, lunches.get(letter));
        }
        editor.commit();
        lunchesHome = lunches;
    }




    public void updateMap()
    {
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE);
            String[] kai= {"A","B","C","D","E","F","G"};
            for (String letter : kai) {
                if (sharedPreferences.contains(letter)) {
                    lunches.put(letter, sharedPreferences.getString(letter, ""));
                }
            }
        }

    }



