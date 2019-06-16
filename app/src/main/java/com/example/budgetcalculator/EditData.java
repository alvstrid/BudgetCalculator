package com.example.budgetcalculator;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class EditData extends AppCompatActivity {

    private Button btnSave,btnDelete;
    private EditText editable_item, editable_item2;
    private Spinner editable_item3;
    Database mDatabaseHelper;

    private String selectedName, selectedAmount, selectedCategory;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        editable_item = (EditText) findViewById(R.id.editable_item);
        mDatabaseHelper = new Database(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        selectedName = receivedIntent.getStringExtra("name");

        //set the text to show the current selected name
        editable_item.setText(selectedName);

        //show current amount
        selectedAmount = receivedIntent.getStringExtra("amount");
        editable_item2 = (EditText) findViewById(R.id.editable_item2);
        editable_item2.setText(selectedAmount);

        //show current category
        selectedCategory =  receivedIntent.getStringExtra("category");
        editable_item3 = findViewById(R.id.expense_category);
        String[] categories = {"Food", "Transportation", "Household", "Health", "Clothes", "Other"};
        for(int i=0; i < categories.length; i++)
          if(selectedCategory.equals(categories[i]))
              editable_item3.setSelection(i);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editable_item.getText().toString();
                String item2 = editable_item2.getText().toString();
                String item3 = editable_item3.getSelectedItem().toString();
                if(!item.equals("")){
                    mDatabaseHelper.updateName(item, item2,item3, selectedID,selectedName);
                }else{
                    toastMessage("You must enter a name");
                }
                finish();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteName(selectedID,selectedName);
                editable_item.setText("");
                toastMessage("Expense removed");
                finish();
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

