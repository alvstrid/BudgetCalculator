package com.example.budgetcalculator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class EditData extends AppCompatActivity {

    private static final String TAG = "EditData";

    private Button btnSave,btnDelete;
    private EditText editable_item, editable_item2;
    private Spinner editable_item3;
    Database mDatabaseHelper;

    private String selectedName, selectedAmount, selectedCategory, selectedDate;
    private int selectedID;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit expense");

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
                    mDatabaseHelper.updateName(item, item2,item3, selectedID,selectedName,selectedAmount,selectedCategory);
                }else{
                    toastMessage("You must enter a name");
                }
                finish();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteName(selectedID,selectedName,selectedAmount,selectedCategory);
                editable_item.setText("");
                toastMessage("Expense removed");
                finish();
            }
        });


        //Calendar

        selectedDate = receivedIntent.getStringExtra("date");
        final TextView expense_date = findViewById(R.id.editable_item3);
        expense_date.setText(selectedDate);


        mDisplayDate = findViewById(R.id.editable_item3);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditData.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };


    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}

