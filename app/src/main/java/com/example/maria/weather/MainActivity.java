package com.example.maria.weather;

import android.accounts.NetworkErrorException;
import android.app.ActionBar;
import android.app.Activity;;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends Activity {
    ActionBar.Tab tab;
    Fragment cityFragment;

    /*
    What is the Content Resolver?
    The Content Resolver is the single, global instance in your application that provides access to your
    (and other applications') content providers. The Content Resolver behaves exactly as its name implies:
    it accepts requests from clients, and resolves these requests by directing them to the content provider
    with a distinct authority. To do this, the Content Resolver stores a mapping from authorities to
    Content Providers. This design is important, as it allows a simple and secure means of accessing other
    applications' Content Providers.
    The Content Resolver includes the CRUD (create, read, update, delete) methods corresponding to the
    abstract methods (insert, query, update, delete) in the Content Provider class. The Content Resolver
    does not know the implementation of the Content Providers it is interacting with (nor does it need to know);
    each method is passed an URI that specifies the Content Provider to interact with.

    What is a Content Provider?
    Whereas the Content Resolver provides an abstraction from the application's Content Providers,
    Content Providers provide an abstraction from the underlying data source (i.e. a SQLite database).
    They provide mechanisms for defining data security (i.e. by enforcing read/write permissions) and offer
    a standard interface that connects data in one process with code running in another process.
    Content Providers provide an interface for publishing and consuming data, based around a simple URI
    addressing model using the content:// schema. They enable you to decouple your application layers from the
    underlying data layers, making your application data-source agnostic by abstracting the underlying data source.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Asking for the default ActionBar element that our platform supports.
        ActionBar actionBar = getActionBar();

        // Creating ActionBar tabs.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        Cursor citiesCursor = getContentResolver().query(Constants.CONTENT_URI_CITIES,null,null,null,null);
        if (!citiesCursor.moveToFirst()) {
            Toast.makeText(this, "no cities yet!", Toast.LENGTH_LONG).show();
        } else {
            ActionBar.Tab cityTab;
            do{
                cityFragment = CityFragment.newInstance(citiesCursor.getInt(
                        citiesCursor.getColumnIndex(Constants.DATABASE_TABLE_CITIES_ID)),
                        citiesCursor.getString(citiesCursor.getColumnIndex(Constants.DATABASE_TABLE_CITIES_NAME)),
                        citiesCursor.getDouble(citiesCursor.getColumnIndex(Constants.DATABASE_TABLE_CITIES_LAT)),
                        citiesCursor.getDouble(citiesCursor.getColumnIndex(Constants.DATABASE_TABLE_CITIES_LON)));
                cityTab = actionBar.newTab();
                cityTab.setText(citiesCursor.getString(citiesCursor.getColumnIndex(Constants.DATABASE_TABLE_CITIES_NAME)));
                // Setting tab listeners.
                cityTab.setTabListener(new TabListener(cityFragment));
                // Adding tabs to the ActionBar.
                actionBar.addTab(cityTab);

            } while (citiesCursor.moveToNext());
        }
/*
       String jsonString = readAssetFile("cities.json");

       try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray citiesArray = jsonObject.getJSONArray("cities");
            ActionBar.Tab cityTab;
            for (int i = 0; i < citiesArray.length(); i++) {
                try {
                    JSONObject city = citiesArray.getJSONObject(i);
                    Log.d("JSON READING", city.getString("name"));
                    cityTab = actionBar.newTab();
                    cityTab.setText(city.getString("name"));

                    cityFragment = CityFragment.newInstance(city);
                    // Setting custom tab icons.
                    tab = actionBar.newTab().setText(city.getString("name"));
                    // Setting tab listeners.
                    tab.setTabListener(new TabListener(cityFragment));
                    // Adding tabs to the ActionBar.
                    actionBar.addTab(tab);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            try {
                refresh();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public String readAssetFile(String fileName) {
        //READ JSON DATA ---------------------------------------------------------------------------
        String jsonString = null;
        InputStream is = null;
        try {
            is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
        //READ JSON DATA END -----------------------------------------------------------------------
    }

    public void refresh() throws JSONException {

            String jsonString = readAssetFile("cities.json");
            ListView listView = (ListView) findViewById(R.id.listView);
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray citiesArray = jsonObject.getJSONArray("cities");
                for (int i = 0; i < citiesArray.length(); i++) {

                        JSONObject city = citiesArray.getJSONObject(i);
                        String stringUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + city.getString("lat") + "&lon=" + city.getString("lon") + "&cnt=10&units=metric&mode=json";
                        new DownloadWebpageTask(this, listView, true).execute(stringUrl, city.getString("name"));
                }

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
