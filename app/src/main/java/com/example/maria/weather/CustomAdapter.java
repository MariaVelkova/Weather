package com.example.maria.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Maria on 1/15/2015.
 */
public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Day> days;
    public CustomAdapter(Context context, ArrayList<Day> days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Day getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return days.get(position).getDt();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView dayView;
        TextView tempView;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.day_row, parent, false);
            dayView = (TextView) convertView.findViewById(R.id.dayView);
            convertView.setTag(R.id.dayView, dayView);
            tempView = (TextView) convertView.findViewById(R.id.tempView);
            convertView.setTag(R.id.tempView, tempView);
        } else {
            dayView = (TextView) convertView.getTag(R.id.dayView);
            tempView = (TextView) convertView.getTag(R.id.tempView);
        }
        Day currentDay = getItem(position);

        Date date = new Date(currentDay.getDt()*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE\nd MMM yyyy"); // the format of your date
        dayOfWeekFormat.setTimeZone(TimeZone.getTimeZone("GMT+2")); // give a timezone reference for formating (see comment at the bottom
        String dayOfWeekString = dayOfWeekFormat.format(date);

        dayView.setText(dayOfWeekString);

        Long min = Math.round(currentDay.getTempMin());
        Long max = Math.round(currentDay.getTempMax());
        tempView.setText(Integer.toString(Integer.valueOf(min.intValue())) + " / " + Integer.toString(Integer.valueOf(max.intValue())) + " C");




        return convertView;
    }
}
