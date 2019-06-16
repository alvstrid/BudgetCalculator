package com.example.budgetcalculator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewExpenses extends AppCompatActivity {

    private static final String TAG = "ViewExpenses";

    Database mDatabase;
    private ListView mListView;

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        populateListView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses);
        mListView = findViewById(R.id.listView);
        mDatabase = new Database(this);
        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = mDatabase.getData();

        ArrayList<String> listData = new ArrayList<>();

        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            //listData.add( (data.getString(1) + " " + data.getString(2).toString()) + " " + data.getString(4) + " " + data.getString(3) );
            listData.add(data.getString(1));
        }
        //create the list adapter and set the adapter
        final ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = mDatabase.getItem(name); //get the id associated with that name
                //Cursor data = mDatabase.getData(name);

                int itemID = -1;
                String amount = "", date = "", category = "";
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                    amount = data.getString(2);
                    date = data.getString(3);
                    category = data.getString(4);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID + " " + amount + " " + date + " " + category);
                    Intent editScreenIntent = new Intent(ViewExpenses.this, EditData.class);
                    editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("name",name);
                    editScreenIntent.putExtra("amount",amount);
                    editScreenIntent.putExtra("date",date);
                    editScreenIntent.putExtra("category",category);
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with that name");
                }

            }
        });
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
