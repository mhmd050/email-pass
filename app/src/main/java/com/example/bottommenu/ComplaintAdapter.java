package com.example.bottommenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ComplaintAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> complaintsList;

    public ComplaintAdapter(Context context, List<String> complaintsList) {
        super(context, android.R.layout.simple_list_item_1, complaintsList);
        this.context = context;
        this.complaintsList = complaintsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView complaintText = convertView.findViewById(android.R.id.text1);
        complaintText.setText(complaintsList.get(position));

        return convertView;
    }
}