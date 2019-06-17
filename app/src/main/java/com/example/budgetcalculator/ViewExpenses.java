package com.example.budgetcalculator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ViewExpenses extends AppCompatActivity {

    private static final String TAG = "ViewExpenses";
    Database mDatabase;

    @Override
    public void onResume()
    {
        super.onResume();
        populateListView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit expense");
        mDatabase = new Database(this);
        populateListView();
        setTitle("All expenses");
    }

    private void populateListView() {

        ArrayList itemsList = getListData();
        final ListView list = (ListView) findViewById(R.id.listExpenses);
        list.setAdapter(new CustomListAdapter(this, itemsList));

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ExpensesItem expense = (ExpensesItem) list.getItemAtPosition(i);
                Log.d(TAG, "Selected item: " + expense.getName() +" " + expense.getAmount() + " " + expense.getCategory());

                    Intent editScreenIntent = new Intent(ViewExpenses.this, EditData.class);
                    editScreenIntent.putExtra("id",expense.getId());
                    editScreenIntent.putExtra("name",expense.getName());
                    editScreenIntent.putExtra("amount",expense.getAmount());
                    editScreenIntent.putExtra("date",expense.getDate());
                    editScreenIntent.putExtra("category",expense.getCategory());
                    startActivity(editScreenIntent);
            }
        });
    }

    private ArrayList getListData() {

        ArrayList<ExpensesItem> listData = new ArrayList<>();
        Cursor data = mDatabase.getData();
        while(data.moveToNext()){

            ExpensesItem item = new ExpensesItem();
            item.setId(data.getInt(0));
            item.setName(data.getString(1));
            item.setCategory(data.getString(4));
            item.setAmount(data.getString(2));
            item.setDate(data.getString(3));
            listData.add(item);
        }

        return listData;
    }
}