package com.example.bottommenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class AppointmentAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> appointments;

    public AppointmentAdapter(Context context, List<String> appointments) {
        super(context, 0, appointments);
        this.context = context;
        this.appointments = appointments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate a new view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Get the data item for this position
        String appointment = getItem(position);

        // Find the TextView in the layout and set the appointment details
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(appointment);

        // Return the completed view to render on the screen
        return convertView;
    }
}