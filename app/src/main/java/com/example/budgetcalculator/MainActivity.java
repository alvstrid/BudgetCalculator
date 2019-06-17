package com.example.budgetcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.card.MaterialCardView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

/**
 * Created by Roman Teodora
 */



public class MainActivity extends AppCompatActivity {

    Database mDatabase;
    ArrayList<Integer> categories = new ArrayList<>(Arrays.asList(R.id.Food, R.id.Transportation, R.id.Household, R.id.Health, R.id.Clothes, R.id.Other));
    final String income_text = "income";
    private static final String TAG = "MyActivity";


    @Override
    public void onResume() {
        super.onResume();
        refreshSums();
        refreshBalance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshSums();
        refreshBalance();

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences settings =getSharedPreferences(income_text,MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();


        final TextView income = findViewById(R.id.income);

        income.setText(pref.getString(income_text, "0"));

        income.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if( s.toString().equals(""))
                s.replace(0,0,"0");

               String s2 = s.toString();

                if(s2.length()>1 && s2.charAt(0) == '0')
                {
                    s.delete(0,1);
                }


                Log.i(TAG, s2);


                pref.edit().putString(income_text, s.toString() ).apply();



                refreshBalance();


            }
        });

        BarChart chart = findViewById(R.id.barchart);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 120f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        entries.add(new BarEntry(4f, 70f));
        entries.add(new BarEntry(5f, 70f));
        entries.add(new BarEntry(6f, 70f));

        int color = getResources().getColor(R.color.colorCharts);
        XAxis xAxis = chart.getXAxis();


        BarDataSet set = new BarDataSet(entries, "Average expense by day");
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setDrawGridLines(false);


        set.setColor(color);
        chart.setScaleEnabled(false);
        chart.animateY(2000);
        chart.getDescription().setEnabled(false);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh


        ArrayList<Integer> card_pictures = new ArrayList<>();
        card_pictures.add(R.drawable.categ_1);
        card_pictures.add(R.drawable.categ_2);
        card_pictures.add(R.drawable.categ_3);
        card_pictures.add(R.drawable.categ_4);
        card_pictures.add(R.drawable.categ_5);
        card_pictures.add(R.drawable.categ_6);

        final String[] category_names = {"Food", "Transportation", "Household", "Health", "Clothes", "Other"};

        int i = 0;

        for (Integer category : categories) {

            final int j = i;
            MaterialCardView card = findViewById(category);
            Button btn = card.findViewById(R.id.add_item);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent myIntent= new Intent(MainActivity.this, AddItem.class);
                    myIntent.putExtra("category_title",category_names[j]);
                    startActivity(myIntent);
                }
            });


            //Set card title

            TextView card_title = card.findViewById(R.id.card_title);
            card_title.setText(category_names[i]);

            //Set card image

            ImageView img = card.findViewById(R.id.card_picture);
            img.setImageResource(card_pictures.get(i));

            i++;


        }
    }

    private void refreshSums() {
        mDatabase = new Database(this);
        Cursor data2 = mDatabase.getSum();
        for (Integer category : categories){
            MaterialCardView card = findViewById(category);
            TextView category_sum = card.findViewById(R.id.category_sum);
            category_sum.setText(data2.getString(0));
            data2.moveToNext();
        }
    }


    public void  refreshBalance(){


        final TextView balance = findViewById(R.id.balance);
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        final SharedPreferences.Editor editor = pref.edit();
        double x,y;
        y = Double.valueOf(mDatabase.getSum2());
        x = Double.valueOf(pref.getString(income_text,"0"));
        double d =  x - y;
        String formattedValue = String.format("%.2f", d);
        balance.setText(formattedValue);
    }



}