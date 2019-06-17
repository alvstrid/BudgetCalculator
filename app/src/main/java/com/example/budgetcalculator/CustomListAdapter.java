package com.example.budgetcalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private ArrayList<ExpensesItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<ExpensesItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.view_expenses_row, null);
            holder = new ViewHolder();
            holder.uName = v.findViewById(R.id.expense_name);
            holder.uDate = v.findViewById(R.id.expense_date);
            holder.uCategory = v.findViewById(R.id.expense_category);
            holder.uAmount = v.findViewById(R.id.expense_amount);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.uName.setText(listData.get(position).getName());
        holder.uDate.setText(listData.get(position).getDate());
        holder.uCategory.setText(listData.get(position).getCategory());
        holder.uAmount.setText(listData.get(position).getAmount());
        return v;
    }

    static class ViewHolder {
        TextView uName;
        TextView uCategory;
        TextView uAmount;
        TextView uDate;
    }
}
