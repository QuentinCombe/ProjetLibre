package fr.univ_tours.polytech.projetlibre.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.univ_tours.polytech.projetlibre.model.Clue;
import fr.univ_tours.polytech.projetlibre.model.Objective;
import fr.univ_tours.polytech.projetlibre.model.User;

/**
 * Created by Alkpo on 21/02/2017.
 */

public class DatabaseHandler
{
    private static DatabaseHandler Inst = new DatabaseHandler();

    private final String ipAdress = "192.168.0.13:80";

    private String scriptToExecute = null;

    private final String baseUrl = "http://" + ipAdress + "/projetlibre/";

    private final int READ_TIMEOUT = 3000;

    private DatabaseHandler()
    {

    }

    public Clue getClueFromId(int idClue)
    {
        Clue clue = null;

        scriptToExecute = "selectClueFromId.php";

        MyAsyncTaskGetClueById getObjectivesAsyncTask = new MyAsyncTaskGetClueById(baseUrl + scriptToExecute);
        getObjectivesAsyncTask.execute(String.valueOf(idClue));

        String result = null;
        try
        {
            result = getObjectivesAsyncTask.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

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

    public static DatabaseHandler getInstance()
    {
        return Inst;
    }

    public List<Objective> getObjectives()
    {
        List<Objective> listObjectives = null;

        scriptToExecute = "getObjectives.php";

        MyAsyncTaskGetString getObjectivesAsyncTask = new MyAsyncTaskGetString(baseUrl + scriptToExecute);
        getObjectivesAsyncTask.execute();

        String result = null;
        try
        {
            result = getObjectivesAsyncTask.get();

        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        if (result != null)
        {
            try
            {
                listObjectives = Objective.convertFromJsonArray(new JSONArray(result));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return listObjectives;
    }


    class MyAsyncTaskGetString extends AsyncTask<Void, Void, String>
    {
        String urlProvided = null;

        public MyAsyncTaskGetString(String url)
        {
            urlProvided = url;
        }

        @SuppressWarnings("Timeout and status to handle")
        @Override
        protected String doInBackground(Void... params)
        {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String result = null;
            InputStream inputStream;

            try
            {
                URL url = new URL(urlProvided);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(3000);
                urlConnection.setRequestMethod("POST");

                urlConnection.connect();

                int status = urlConnection.getResponseCode();

                if (status == HttpURLConnection.HTTP_OK)
                {
                    // Read the input stream into a String
                    inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null)
                    {
                        // Nothing to do.
                        return null;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0)
                    {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    result = buffer.toString();
                }

                return result;
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
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }

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

                Log.v("getObjectives", "Status = " + status);

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

    public User getUserFromId(String mail, String password){
        User user = null;
        Log.d(toString(),"GetUserFromID");
        scriptToExecute = "getUserFromIdentifier.php";

        MyAsyncTaskGetUserFromIdentifier getUserFromIdentifierAsyncTask = new MyAsyncTaskGetUserFromIdentifier(baseUrl + scriptToExecute);
        Log.d(toString(),mail+"\n"+password);
        getUserFromIdentifierAsyncTask.execute(mail,password);

        String result = null;
        try
        {
            result = getUserFromIdentifierAsyncTask.get();
            Log.d(toString(),result);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        if (result != null)
        {
            try
            {
                user = User.convertFromJsonObject(new JSONObject(result));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return user;
    }
    class MyAsyncTaskGetUserFromIdentifier extends AsyncTask<String, String, String> {

        String urlProvided = null;

        public MyAsyncTaskGetUserFromIdentifier(String url)
        {
            Log.d(toString(),"HERE");
            urlProvided = url;
        }

        @Override
        protected void onPreExecute() {

            Log.d(toString(),"HERE In myAsynchTask");
        }
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            Log.d(toString(),"param : \n"+params[0]+"\n"+params[1]);
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
                        .appendQueryParameter("mail", params[0])
                        .appendQueryParameter("password",params[1]);

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
                    Log.d(toString(),"Result :"+result.toString());
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
            Log.d(toString(),"RECIEVED : "+s);
            super.onPostExecute(s);
        }
    }

}
