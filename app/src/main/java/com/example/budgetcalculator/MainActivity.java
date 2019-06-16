package com.example.budgetcalculator;


import android.content.Intent;
import android.os.Bundle;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.card.MaterialCardView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //CHART

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


        BarDataSet set = new BarDataSet(entries, "Daily expenses");
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
        //CHART END

        ArrayList<Integer> categories = new ArrayList<>(Arrays.asList(R.id.Food, R.id.Transportation, R.id.Household, R.id.Health, R.id.Clothes, R.id.Other));
        String[] category_names = {"Food", "Transportation", "Household", "Health", "Clothes", "Other"};
        ArrayList<Integer>card_pictures = new ArrayList<>();
        card_pictures.add(R.drawable.categ_1);
        card_pictures.add(R.drawable.categ_2);
        card_pictures.add(R.drawable.categ_3);
        card_pictures.add(R.drawable.categ_4);
        card_pictures.add(R.drawable.categ_5);
        card_pictures.add(R.drawable.categ_6);

        int i = 0;
        for(Integer category : categories){
            MaterialCardView card = findViewById(category);
            Button btn = card.findViewById(R.id.add_item);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, AddItem.class));
                }
            });

            //Set card title

            TextView card_title = card.findViewById(R.id.card_title);
            card_title.setText(category_names[i]);

            //Set card image

            ImageView img= card.findViewById(R.id.card_picture);
            img.setImageResource(card_pictures.get(i));

            i++;

        }

    }

}

