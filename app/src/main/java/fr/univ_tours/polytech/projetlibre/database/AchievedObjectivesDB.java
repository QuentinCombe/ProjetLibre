package fr.univ_tours.polytech.projetlibre.database;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import fr.univ_tours.polytech.projetlibre.model.Objective;

/**
 * Created by Alkpo on 16/03/2017.
 */

public class AchievedObjectivesDB extends DatabaseHandler
{
    protected static AchievedObjectivesDB Inst = new AchievedObjectivesDB();

    protected AchievedObjectivesDB()
    {

    }

    public static AchievedObjectivesDB getInstance()
    {
        return Inst;
    }

    public void insertAchievedObjective(int idUser, int idObjective)
    {
        scriptToExecute = "scripts/insertAchievedObjective.php";

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

    public ArrayList<Objective> getAchievedObjectivesById(int idUser)
    {
        ArrayList<Objective> achievedObjectives = new ArrayList<Objective>();

        scriptToExecute = "scripts/getAchievedObjectivesById.php";

        AchievedObjectivesDB.MyAsyncTaskGetAchievedObjectives getAchievedObjectivesAsyncTask = new AchievedObjectivesDB.MyAsyncTaskGetAchievedObjectives(baseUrl + scriptToExecute);
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

    static class MyAsyncTaskGetAchievedObjectives extends AsyncTask<Integer, String, String>
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
