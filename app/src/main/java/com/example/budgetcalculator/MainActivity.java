package com.example.budgetcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.card.MaterialCardView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
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
    private Button btnViewData;
    ArrayList<Float> sums = new ArrayList<>();
    BarChart chart;
    List<BarEntry> entries = new ArrayList<>();
    BarDataSet set;


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
        BarChart chart = findViewById(R.id.barchart);

        set = new BarDataSet(entries, "");

        refreshSums();
        refreshBalance();

        final EditText income = findViewById(R.id.income);

        findViewById(R.id.scrollView2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnViewData.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                income.clearFocus();
                return false;
            }
        });

       income.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        income.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(btnViewData.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });


        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences settings =getSharedPreferences(income_text,MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();


        btnViewData = findViewById(R.id.view_expenses);

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewExpenses.class);
                startActivity(intent);
            }
        });



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

        BarChart chart = findViewById(R.id.barchart);
        sums.clear();
        entries.clear();

        mDatabase = new Database(this);
        Cursor data2 = mDatabase.getSum();
        for (Integer category : categories){
            MaterialCardView card = findViewById(category);
            TextView category_sum = card.findViewById(R.id.category_sum);
            category_sum.setText(data2.getString(0));
            sums.add(Float.parseFloat(data2.getString(0)));
            data2.moveToNext();
        }

        entries.add(new BarEntry(0f, sums.get(0)));
        entries.add(new BarEntry(1f, sums.get(1)));
        entries.add(new BarEntry(2f, sums.get(2)));
        entries.add(new BarEntry(3f, sums.get(3)));
        entries.add(new BarEntry(4f, sums.get(4)));
        entries.add(new BarEntry(5f, sums.get(5)));

        XAxis xAxis = chart.getXAxis();
        String[] days = {"", "", "", "", "", ""};



        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setDrawGridLines(false);
        xAxis.enableAxisLineDashedLine(2,2,2);

        YAxis rightAxis = chart.getAxisRight();
        YAxis leftAxis = chart.getAxisLeft();
        rightAxis.setDrawLabels(false);
        leftAxis.setDrawLabels(false);

        chart.setScaleEnabled(false);
        chart.animateY(2000);
        chart.getDescription().setEnabled(false);

        set = new BarDataSet(entries,"Expenses by category");
        int[] colors = new int[] {Color.rgb(93, 144, 186), Color.rgb(32, 99, 155), Color.rgb(60, 174, 163), Color.rgb(246, 213, 92), Color.rgb(237, 85, 59), Color.rgb(171, 113, 198),Color.rgb(96, 175, 169)};
        set.setColors(colors);

        Legend l = chart.getLegend();
        LegendEntry l1=new LegendEntry("Food", Legend.LegendForm.DEFAULT,10f,2f,null, Color.rgb(93, 144, 186));
        LegendEntry l2=new LegendEntry("Transportation", Legend.LegendForm.DEFAULT,10f,2f,null, Color.rgb(32, 99, 155));
        LegendEntry l3=new LegendEntry("Household", Legend.LegendForm.DEFAULT,10f,2f,null, Color.rgb(60, 174, 163));
        LegendEntry l4=new LegendEntry("Health", Legend.LegendForm.DEFAULT,10f,2f,null, Color.rgb(246, 213, 92));
        LegendEntry l5=new LegendEntry("Clothes", Legend.LegendForm.DEFAULT,10f,2f,null, Color.rgb(237, 85, 59));
        LegendEntry l6=new LegendEntry("Other", Legend.LegendForm.DEFAULT,10f,2f,null, Color.rgb(171, 113, 198));
        l.setCustom(new LegendEntry[]{l1,l2,l3,l4,l5,l6});



        chart.getLegend().setWordWrapEnabled(true);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.notifyDataSetChanged();
        chart.invalidate(); // refresh



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