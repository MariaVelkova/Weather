package com.example.maria.weather;

import android.accounts.NetworkErrorException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Maria on 1/13/2015.
 */
// Uses AsyncTask to create a task away from the main UI thread. This task takes a
// URL string and uses it to create an HttpUrlConnection. Once the connection
// has been established, the AsyncTask downloads the contents of the webpage as
// an InputStream. Finally, the InputStream is converted into a string, which is
// displayed in the UI by the AsyncTask's onPostExecute method.
//public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
    private static final String DEBUG_TAG = "NetworkStatusExample";

    private Context context;
    File outputDir;
    File outputFile;
    ListView listView;
    Boolean doRefresh = false;
    int cityId = 0;
    public DownloadWebpageTask(Context context, ListView listView, Boolean doRefresh) {
        this.context = context;
        this.outputDir = context.getCacheDir(); // context being the Activity pointer
        this.listView = listView;
        this.doRefresh = doRefresh;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("DOWNLOAD", "Starting now...");
    }




    @Override
    protected String doInBackground(String... params) {
        // params comes from the execute() call: params[0] is the url.
        String result = "";
        try {
            URL url = new URL(params[0]);
            Log.d("URL", params[0]);
            cityId = Integer.parseInt(params[1]);
//            String cityName = params[1];
//            outputFile = new File(outputDir.getPath() + "/" + cityName + ".json");
//            Log.d("File Name", outputFile.getAbsolutePath());
//            result =  downloadUrl(url);
            result =  downloadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
            return "Unable to retrieve web page. URL may be invalid.";
        } catch (NetworkErrorException e) {
            e.printStackTrace();
        }
        return  result;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {

        Log.d("DOWNLOAD", "Just finished - " + result);
        /*
        ArrayList<Day> days = new ArrayList<Day>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            Double message = jsonObject.getDouble("message");
            JSONObject city = jsonObject.getJSONObject("city");
            JSONObject coord = city.getJSONObject("coord");
            Double lat = coord.getDouble("lat");
            Double lon = coord.getDouble("lon");
            int fff= 0;
            JSONArray list = jsonObject.getJSONArray("list");
            for(int i = 0; i < list.length(); i++) {
                JSONObject day = list.getJSONObject(i);
                int dt = day.getInt("dt");
                int humidity = day.getInt("humidity");
                Double pressure = day.getDouble("pressure");
                Double speed = day.getDouble("speed");
                JSONArray weather = day.getJSONArray("weather");
                String icon = "";
                String description = "";
                String main = "";
                for(int ii = 0; ii < weather.length(); ii++) {
                    JSONObject item = weather.getJSONObject(ii);
                    icon = item.getString("icon");
                    description = item.getString("description");
                    main = item.getString("main");
                    int ppp = 0;
                }
                JSONObject temp = day.getJSONObject("temp");
                Double min = temp.getDouble("min");
                Double max = temp.getDouble("max");
                int pp = 0;
                Day dayModel = new Day(dt,lat, lon, humidity, pressure, speed, icon, description, main, min, max );
                days.add(dayModel);
            }
            int cnt = jsonObject.getInt("cnt");
            String cod = jsonObject.getString("cod");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomAdapter adapter = new CustomAdapter(context, days);
        listView.setAdapter(adapter);
        */
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(URL url) throws IOException, NetworkErrorException {
        Cursor daysCursor = context.getContentResolver().query(Constants.CONTENT_URI_DAYS,null,"city_id = ?",new String[] {Integer.toString(cityId)},Constants.DATABASE_TABLE_DAYS_DT);
        if (!daysCursor.moveToFirst()) {
            InputStreamReader byteReader;
            InputStream inputStream = null;
            String resultString = "";
//        if (outputFile.length() != 0 && doRefresh == false) {
//            Log.d("READ FROM","CACHE");
//            byteReader = new FileReader(outputFile);
//        } else {
                if (hasNetworkConnection()) {
                    Log.d("READ FROM", "WEB");
                    HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                    HttpGet httpGet = new HttpGet(String.valueOf(url));
                    HttpResponse response = httpclient.execute(httpGet);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    byteReader = new InputStreamReader(inputStream, "UTF-8");
                } else {
                    throw new NetworkErrorException("No network connection available.");
                }
//        }


                resultString = createString(byteReader);


//        if (outputFile.length() == 0 || doRefresh == true) {
//            writeCacheFile(resultString);
//        }

        if(inputStream != null && inputStream.available() != 0) {
            inputStream.close();
        }

        updateForecast(resultString);

//        return resultString;
        }
        return "Success";
        /*
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); //milliseconds
            conn.setConnectTimeout(15000);//milliseconds
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("HttpExample", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
        */
    }

    public void updateForecast(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Double message = jsonObject.getDouble("message");
            JSONObject city = jsonObject.getJSONObject("city");
            JSONObject coord = city.getJSONObject("coord");
            Double lat = coord.getDouble("lat");
            Double lon = coord.getDouble("lon");
            int cnt = jsonObject.getInt("cnt");
            String cod = jsonObject.getString("cod");

            JSONArray list = jsonObject.getJSONArray("list");
            for(int i = 0; i < list.length(); i++) {
                JSONObject day = list.getJSONObject(i);
                int dt = day.getInt("dt");
                int humidity = day.getInt("humidity");
                Double pressure = day.getDouble("pressure");
                Double speed = day.getDouble("speed");
                JSONArray weather = day.getJSONArray("weather");
                String icon = "";
                String description = "";
                String main = "";
                for(int ii = 0; ii < weather.length(); ii++) {
                    JSONObject item = weather.getJSONObject(ii);
                    icon = item.getString("icon");
                    description = item.getString("description");
                    main = item.getString("main");
                }
                JSONObject temp = day.getJSONObject("temp");
                Double min = temp.getDouble("min");
                Double max = temp.getDouble("max");
                ContentValues values = new ContentValues();
                values.put(Constants.DATABASE_TABLE_DAYS_DT, dt);
                values.put(Constants.DATABASE_TABLE_DAYS_CITY_ID, cityId);
                values.put(Constants.DATABASE_TABLE_DAYS_MIN, min);
                values.put(Constants.DATABASE_TABLE_DAYS_MAX, max);
                Uri uri = context.getContentResolver().insert(Constants.CONTENT_URI_DAYS, values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public String createString(InputStreamReader byteReader) throws IOException {
        String resultString = "";
        BufferedReader reader = new BufferedReader(byteReader, 8);
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        resultString = sb.toString();
        return resultString;
    }



    public void writeCacheFile(String contentString) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(outputFile);

            /** Saving the contents to the file*/
            writer.write(contentString);

            /** Closing the writer object */
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        Log.d(DEBUG_TAG, "Wifi connected: " + isWifiConn);
        Log.d(DEBUG_TAG, "Mobile connected: " + isMobileConn);

        networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
