package com.example.maria.weather;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Maria on 1/14/2015.
 */
public class CityFragment extends Fragment {


    // Create a new instance of CityFragment
//    public static CityFragment newInstance(JSONObject city) {
//        CityFragment f = new CityFragment();
//        Bundle args = new Bundle();
//        args.putString("city", city.toString());
//        f.setArguments(args);
//        return f;
//    }

//    public JSONObject getCity() throws JSONException {
//        return new JSONObject(getArguments().getString("city", ""));
//    }

    public static CityFragment newInstance(int cityId, String cityName, Double cityLat, Double cityLon) {
        CityFragment f = new CityFragment();
        Bundle args = new Bundle();
        args.putInt("cityId", cityId);
        args.putString("cityName", cityName);
        args.putDouble("cityLat", cityLat);
        args.putDouble("cityLon", cityLon);
        f.setArguments(args);
        return f;
    }

    public int getCityId() {
        return getArguments().getInt("cityId", 0);
    }

    public String getCityName() {
        return getArguments().getString("cityName", "");
    }

    public Double getCityLat() {
        return getArguments().getDouble("cityLat", 0);
    }

    public Double getCityLon() {
        return getArguments().getDouble("cityLon", 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;

    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();

        ListView listView = (ListView) activity.findViewById(R.id.listView);
//        try {
//            JSONObject city = getCity();
//            String cityName = city.getString("name");
//            String cityLat = city.getString("lat");
//            String cityLon = city.getString("lon");
            String cityName = getCityName();
            String cityLat = Double.toString(getCityLat());
            String cityLon = Double.toString(getCityLon());
            Log.d("CITY", cityName);
            //fetch data

            Cursor daysCursor = activity.getContentResolver().query(Constants.CONTENT_URI_DAYS,null,"city_id = ?",new String[] {Integer.toString(getCityId())},Constants.DATABASE_TABLE_DAYS_DT);
            if (!daysCursor.moveToFirst()) {
                String stringUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + cityLat + "&lon=" + cityLon + "&cnt=10&units=metric&mode=json";
                new DownloadWebpageTask(activity, listView, false).execute(stringUrl, Integer.toString(getCityId()));
            } else {
                Date today = new Date(); // *1000 is to convert seconds to milliseconds
                SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE\nd MMM yyyy"); // the format of your date
                dayOfWeekFormat.setTimeZone(TimeZone.getTimeZone("GMT+2")); // give a timezone reference for formating (see comment at the bottom
                String todayString = dayOfWeekFormat.format(today);
                ArrayList<Day> days = new ArrayList<Day>();
                Boolean found = false;
                do{

                     int dt = daysCursor.getInt(daysCursor.getColumnIndex(Constants.DATABASE_TABLE_DAYS_DT));
                     Date dayDate = new Date(dt*1000L); // *1000 is to convert seconds to milliseconds
                     String dateString = dayOfWeekFormat.format(dayDate);
                     if (today.equals(dateString)) {
                         found = true;
                     }
                     Day dayModel = new Day(dt,0.0, 0.0, 0, 0.0, 0.0, "", "", "",daysCursor.getDouble(daysCursor.getColumnIndex(Constants.DATABASE_TABLE_DAYS_MIN)), daysCursor.getDouble(daysCursor.getColumnIndex(Constants.DATABASE_TABLE_DAYS_MAX)) );
                     days.add(dayModel);
                } while (daysCursor.moveToNext());
               // if (found  == true) {
                    CustomAdapter adapter = new CustomAdapter(activity, days);
                    listView.setAdapter(adapter);
               // }
            }
//            new DownloadWebpageTask(activity, listView, false).execute(stringUrl, cityName);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }


}
