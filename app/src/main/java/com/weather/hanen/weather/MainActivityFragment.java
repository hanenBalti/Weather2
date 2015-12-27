package com.weather.hanen.weather;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.weather.hanen.weather.Sqlite.Weather;
import com.weather.hanen.weather.Sqlite.WeatherDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    protected ArrayAdapter<String> mForecastAdapter;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //récuperation de la vue de main fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Déclaration  de valeurs statique pour notre listView

        WeatherDBHelper dbHelper = new WeatherDBHelper(getActivity());
        dbHelper.getWritableDatabase();


        dbHelper.insertWeather(new Weather("Sunday","it's too cold ","10/12"));
        String[] forecastArray = {
                "Today-Sunny-88/63",
                "Tomorrow-Foggy-70/40",
                "Weds-Cloudy-72/63",
                "Thurs-Cloudy-31/63",
                "Fri-Heavy Rain -65/56",
                "Sat- Sunny-88/63",
                "Sun-Sunny-80/68"

        };
        List<String> weekforecast = new ArrayList<String>(Arrays.asList(forecastArray));// conversion du tableau vers une liste
        //Recuperation de la listView à partir du code XML +Casting
        ListView listViewforcast = (ListView) rootView.findViewById(R.id.listview_forecast);
        // Creation d'un adaptateur pour notre listView

        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekforecast);
        // attribution arrayAdaptateur au listView
        listViewforcast.setAdapter(mForecastAdapter);
        //_____________________//
        FetchWeatherTask task = new FetchWeatherTask();
        task.execute();
        listViewforcast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        getActivity(),
                        "vous avez cliqué sur la position"+position,
                        Toast.LENGTH_LONG)
                        .show();
                Intent DetailIntent=new Intent(getActivity(),DetailsActivity.class);
                String listItem=(String)parent.getItemAtPosition(position);
                DetailIntent.putExtra("clé",listItem);
                        startActivity(DetailIntent);
            }
        });

        return rootView;
    }

    // nouvelle class
    class FetchWeatherTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected void onPostExecute(String[] forecasts) {
            super.onPostExecute(forecasts);
            Log.i("PostExecuteResult:", Arrays.toString(forecasts));
            Toast.makeText(
                    getActivity(),
                    Arrays.toString(forecasts),
                    Toast.LENGTH_LONG)
            .show();
            mForecastAdapter.clear();
            mForecastAdapter.addAll(forecasts);
            mForecastAdapter.notifyDataSetChanged();

        }

        @Override
        protected String[] doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String[] result =null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Tunis&mode=json&units=metric&cnt=3&appid=2de143494c0b295cca9337e1e96b00e0");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
               result = getWeatherDataFromJson(forecastJsonStr, 3);
                Log.i("mainActivityFragment:", "result:" + Arrays.toString(result));

              /*  mForecastAdapter.clear();
                mForecastAdapter.addAll(result);
               mForecastAdapter.notifyDataSetChanged();*/


            } catch (IOException e) {
                Log.e("Error", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                forecastJsonStr = null;
            } catch (JSONException e) {
                Log.e("MainActivityFragment", e.getMessage());
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            //Log.i("Resultat de lAPI",forecastJsonStr);

            return result;

        }

                /* The date/time conversion code is going to be moved outside the asynctask later,
 * so for convenience we're breaking it out into its own method now.
 */

        private String getReadableDateString(long time) {
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            Date date = new Date(time * 1000);
            SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
            return format.format(date).toString();
        }

        /**
         * Prepare the weather high/lows for presentation.
         */
        private String formatHighLows(double high, double low) {
            // For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "list";
            final String OWM_WEATHER = "weather";
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";
            final String OWM_DATETIME = "dt";
            final String OWM_DESCRIPTION = "main";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            String[] resultStrs = new String[numDays];
            for (int i = 0; i < weatherArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String day;
                String description;
                String highAndLow;

                // Get the JSON object representing the day
                JSONObject dayForecast = weatherArray.getJSONObject(i);

                // The date/time is returned as a long.  We need to convert that
                // into something human-readable, since most people won't read "1400356800" as
                // "this saturday".
                long dateTime = dayForecast.getLong(OWM_DATETIME);
                day = getReadableDateString(dateTime);

                // description is in a child array called "weather", which is 1 element long.
                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.
                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                double high = temperatureObject.getDouble(OWM_MAX);
                double low = temperatureObject.getDouble(OWM_MIN);

                highAndLow = formatHighLows(high, low);
                resultStrs[i] = day + " - " + description + " - " + highAndLow;
            }

            return resultStrs;
        }
    }
}