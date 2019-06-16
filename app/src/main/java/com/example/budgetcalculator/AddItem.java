package com.example.budgetcalculator;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class AddItem extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Context context = this;

    //Database
    Database expensesDatabase;

    private Button btnAdd, btnViewData;
    private EditText editText;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add item");

        final View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);


        final EditText expense_name = findViewById(R.id.expense_name);
        final EditText expense_amount = findViewById(R.id.expense_amount);
        final TextView expense_date = findViewById(R.id.expense_date);
        final Spinner expense_category = findViewById(R.id.expense_category);

        btnAdd = findViewById(R.id.add_expense);
        btnViewData = findViewById(R.id.view_expenses);
        expensesDatabase = new Database(this);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat current_date1 = new SimpleDateFormat("dd/MM/yyyy");
        String current_date = current_date1.format(cal.getTime());
        expense_date.setText(current_date.toString());


        mDisplayDate = findViewById(R.id.expense_date);

        //Calendar
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddItem.this,
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

        //DATABASE
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newName = expense_name.getText().toString();
                String newAmount = expense_amount.getText().toString();
                String newCategory = expense_category.getSelectedItem().toString();
                String newDate = expense_date.getText().toString();

                if (expense_name.length() != 0 && expense_amount.length()!= 0) {
                    AddData(newName,newAmount,newCategory,newDate);
                    expense_name.setText("");
                    expense_amount.setText("");

                } else {
                    toastMessage("Complete all fields!");
                }

            }
        });


        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItem.this, ViewExpenses.class);
                startActivity(intent);
            }
        });

    }


    public void AddData(String expense, String amount, String category,String date) {
        boolean insertData = expensesDatabase.addData(expense,amount,category,date);
        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    }



