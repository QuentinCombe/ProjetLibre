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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.Clue;
import fr.univ_tours.polytech.projetlibre.model.Objective;
import fr.univ_tours.polytech.projetlibre.model.User;

/**
 * Created by Alkpo on 21/02/2017.
 */

public class DatabaseHandler
{
    private static DatabaseHandler Inst = new DatabaseHandler();
	
    private String scriptToExecute = null;
    private final String baseUrl = "http://" + "192.168.1.12:8080" + "/projetlibre/";

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

        Log.v(toString(), "Resultat = " + result);

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
                    InputStream input = urlConnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());



                }

                return null;
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
        scriptToExecute = "selectUserFromIdentifier.php";

        MyAsyncTaskGetUserFromIdentifier getUserFromIdentifierAsyncTask = new MyAsyncTaskGetUserFromIdentifier(baseUrl + scriptToExecute);
        Log.d(toString(),mail+"\n"+password);
        getUserFromIdentifierAsyncTask.execute(mail,password);

        String result = null;
        try
        {
            result = getUserFromIdentifierAsyncTask.get();

            Log.v(toString(), result);
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


    public void insertAchievedObjective(int idUser, int idObjective)
    {
        scriptToExecute = "insertAchievedObjective.php";

        MyAsyncTaskInsertIntoAchievedObjectives insertIntoAchievedObjectivesAsynTask = new MyAsyncTaskInsertIntoAchievedObjectives(baseUrl + scriptToExecute);
        insertIntoAchievedObjectivesAsynTask.execute(idUser, idObjective);

        Log.v(toString(), "Is everything OKAY with inset into achived ?");

        try
        {
            Log.v(toString(), insertIntoAchievedObjectivesAsynTask.get());
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    class MyAsyncTaskInsertIntoAchievedObjectives extends AsyncTask<Integer, Void, String>
    {
        String urlProvided = null;

        public MyAsyncTaskInsertIntoAchievedObjectives(String url)
        {
            urlProvided = url;
        }

        @Override
        protected String doInBackground(Integer... params)
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
                        .appendQueryParameter("idUser", String.valueOf(params[0]))
                        .appendQueryParameter("idObjective", String.valueOf(params[1]));

                Log.v(this.toString(), "Contenu de la query = " + builder.toString());

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

                Log.v(toString(), "Reponse = " + response_code);

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK)
                {

                    return "success";

                }
                else
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

    public ArrayList<Objective> getAchievedObjectivesById(int idUser)
    {
        ArrayList<Objective> achievedObjectives = null;

        scriptToExecute = "getAchievedObjectivesById.php";

        MyAsyncTaskGetAchievedObjectives getAchievedObjectivesAsyncTask = new MyAsyncTaskGetAchievedObjectives(baseUrl + scriptToExecute);
        getAchievedObjectivesAsyncTask.execute(idUser);

        String result = null;
        try
        {
            result = getAchievedObjectivesAsyncTask.get();
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
                achievedObjectives = Objective.convertListAchievedObjectivesFromJSONArray(new JSONArray(result));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return achievedObjectives;
    }

    class MyAsyncTaskGetAchievedObjectives extends AsyncTask<Integer, String, String>
    {
        String urlProvided = null;

        public MyAsyncTaskGetAchievedObjectives(String url)
        {
            urlProvided = url;
        }

        @Override
        protected String doInBackground(Integer... params)
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
                        .appendQueryParameter("idUser", String.valueOf(params[0]));

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
