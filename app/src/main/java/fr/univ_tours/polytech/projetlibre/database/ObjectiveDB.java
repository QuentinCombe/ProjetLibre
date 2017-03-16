package fr.univ_tours.polytech.projetlibre.database;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.univ_tours.polytech.projetlibre.model.Objective;

/**
 * Created by Alkpo on 16/03/2017.
 */

public class ObjectiveDB extends DatabaseHandler
{
    protected static ObjectiveDB Inst = new ObjectiveDB();

    protected ObjectiveDB()
    {

    }

    public static ObjectiveDB getInstance()
    {
        return Inst;
    }

    public List<Objective> getObjectives()
    {
        List<Objective> listObjectives = null;

        scriptToExecute = "scripts/getObjectives.php";

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
}
