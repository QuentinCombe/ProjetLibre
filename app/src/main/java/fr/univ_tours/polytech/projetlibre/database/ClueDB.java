package fr.univ_tours.polytech.projetlibre.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import fr.univ_tours.polytech.projetlibre.model.Clue;

/**
 * Created by Alkpo on 16/03/2017.
 */

public class ClueDB extends DatabaseHandler
{
    protected static ClueDB Inst = new ClueDB();

    protected ClueDB()
    {

    }

    public static ClueDB getInstance()
    {
        return Inst;
    }

    public Clue getClueFromId(int idClue)
    {
        Clue clue = null;

        scriptToExecute = "scripts/selectClueFromId.php";

        MyAsyncTaskGetClueById getClueByIdAsyncTask = new MyAsyncTaskGetClueById(baseUrl + scriptToExecute);
        getClueByIdAsyncTask.execute(String.valueOf(idClue));

        String result = null;
        try
        {
            result = getClueByIdAsyncTask.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        Log.v(toString(), "Le resultat de getcluefromid est " + result);

        if (result != null)
        {
            try
            {
                clue = Clue.convertFromJsonObject(new JSONObject(result));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return clue;
    }





    public Bitmap getBitmapClueFromName(String imageName)
    {
        Bitmap bitmap = null;
        scriptToExecute = "clues/" + imageName + ".jpg";

        MyAsyncTaskGetBitmap getClueImageAsyncTask = new MyAsyncTaskGetBitmap(baseUrl + scriptToExecute);
        getClueImageAsyncTask.execute();

        try
        {
            bitmap = getClueImageAsyncTask.get();

            return bitmap;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        return bitmap;

    }


    class MyAsyncTaskGetBitmap extends AsyncTask<Void, Void, Bitmap>
    {
        String urlProvided = null;

        public MyAsyncTaskGetBitmap(String url)
        {
            urlProvided = url;
        }

        @SuppressWarnings("Timeout and status to handle")
        @Override
        protected Bitmap doInBackground(Void... params)
        {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            Bitmap bitmap = null;
            InputStream inputStream = null;

            try
            {
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

                Log.v(toString(), "Status = " + status + " avec script = " + scriptToExecute);

                if (status == HttpURLConnection.HTTP_OK)
                {
                    // Read the input stream into a String
                    inputStream = urlConnection.getInputStream();

                    bitmap = BitmapFactory.decodeStream(inputStream);
                }

                return bitmap;
            }
            catch (ProtocolException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }
            finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                }
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch (final IOException e)
                    {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);
        }

    }

    class MyAsyncTaskGetClueById extends AsyncTask<String, String, String>
    {
        String urlProvided = null;

        public MyAsyncTaskGetClueById(String url)
        {
            urlProvided = url;
        }

        @Override
        protected String doInBackground(String... params)
        {
            HttpURLConnection conn;

            try
            {
                URL url = new URL(urlProvided);

                conn = (HttpURLConnection) url.openConnection();
                // conn.setReadTimeout(READ_TIMEOUT);
                //conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", params[0]);

                Log.v(this.toString(), builder.toString());

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try
            {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK)
                {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else
                {

                    return ("unsuccessful");
                }

            }
            catch (IOException e)
            {
                e.printStackTrace();
                return "exception";
            }
            finally
            {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }
}
