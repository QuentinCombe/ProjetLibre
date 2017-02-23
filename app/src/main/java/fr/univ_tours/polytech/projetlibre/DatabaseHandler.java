package fr.univ_tours.polytech.projetlibre;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.univ_tours.polytech.projetlibre.model.Objective;

/**
 * Created by Alkpo on 21/02/2017.
 */

public class DatabaseHandler
{
    private static DatabaseHandler Inst = new DatabaseHandler();

    private String ipAdress = "192.168.1.12:8080";
    private String scriptToExecute = null;
    private String urlProvided = null;

    private DatabaseHandler()
    {

    }

    public static DatabaseHandler getInstance()
    {
        return Inst;
    }

    public JSONObject getObjectives()
    {
        scriptToExecute = "getObjectives.php";
        urlProvided = "http://" + ipAdress + "/projetlibre/" + scriptToExecute;

        System.out.print("URL = " + urlProvided);

        MyAsyncTask getObjectivesAsyncTask = new MyAsyncTask(urlProvided);
        getObjectivesAsyncTask.execute();

        String result = null;
        try {
            result = getObjectivesAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject obj = null;

        if (result != null)
        {
            try {
                JSONArray array = new JSONArray(result);

                obj = array.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    class MyAsyncTask extends AsyncTask<Void, Void, String>
    {
        String urlProvided = null;

        public MyAsyncTask(String url)
        {
            urlProvided = url;
        }

        @SuppressWarnings("Timeout and status to handle")
        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL(urlProvided);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(3000);
                urlConnection.setRequestMethod("POST");

                urlConnection.connect();

                int status = urlConnection.getResponseCode();

                System.out.println(status + " ");

                if (status == 200)
                {
                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
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
                        return null;
                    }
                    result = buffer.toString();
                }

                return result;
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
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

            return "";
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }

    }
}
