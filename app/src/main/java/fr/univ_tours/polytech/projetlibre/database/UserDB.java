package fr.univ_tours.polytech.projetlibre.database;

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
import java.net.URL;
import java.util.concurrent.ExecutionException;

import fr.univ_tours.polytech.projetlibre.model.GlobalDatas;
import fr.univ_tours.polytech.projetlibre.model.User;

/**
 * Created by Alkpo on 16/03/2017.
 */

public class UserDB extends DatabaseHandler
{
    protected static UserDB Inst = new UserDB();

    protected UserDB()
    {

    }

    public static UserDB getInstance()
    {
        return Inst;
    }

    public User getUserFromId(String mail, String password){
        User user = null;
        scriptToExecute = "scripts/selectUserFromIdentifier.php";

        MyAsyncTaskGetUserFromIdentifier getUserFromIdentifierAsyncTask = new MyAsyncTaskGetUserFromIdentifier(baseUrl + scriptToExecute);
        Log.v(toString(),mail+"\n"+password);
        getUserFromIdentifierAsyncTask.execute(mail,password);

        String result = null;
        try
        {
            result = getUserFromIdentifierAsyncTask.get();

            Log.v(toString(), "Resultat = " + result);
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




    class MyAsyncTaskGetUserFromIdentifier extends AsyncTask<String, String, String>
    {

        String urlProvided = null;

        public MyAsyncTaskGetUserFromIdentifier(String url)
        {
            Log.v(toString(),"HERE");
            urlProvided = url;
        }

        @Override
        protected void onPreExecute() {

            Log.v(toString(),"HERE In myAsynchTask");
        }
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn;
            Log.v(toString(),"param : \n"+params[0]+"\n"+params[1]);
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

                Log.v(this.toString(), "Resultat du builder = " + builder.toString());

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
                    Log.v(toString(),"Result de la requete  :"+result.toString());
                    return (result.toString());
                } else
                {
                    Log.v(toString(), "Code derreur = " + response_code);
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
            Log.v(toString(),"RECIEVED : "+s);
            super.onPostExecute(s);
        }
    }

    public void insertUser(String username, String email, String password)
    {
        scriptToExecute = "scripts/InsertNewUser.php";

        MyAsyncTaskInsertUser insertNewUser = new MyAsyncTaskInsertUser(baseUrl + scriptToExecute);
        insertNewUser.execute(username, email, password);

        Log.v(toString(), "Is everything OKAY with inset into achived ?");

        try
        {
            Log.v(toString(), insertNewUser.get());
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

    class MyAsyncTaskInsertUser extends AsyncTask<String, Void, String>
    {
        String urlProvided = null;

        public MyAsyncTaskInsertUser(String url)
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
                        .appendQueryParameter("username", String.valueOf(params[0]))
                        .appendQueryParameter("mail", String.valueOf(params[1]))
                        .appendQueryParameter("password", String.valueOf(params[2]));

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

    public void updateUserExperience(int idUser, int exp)
    {
        scriptToExecute = "scripts/updateUserExperience.php";

        MyAsyncTaskUpdateUserExperience updateUserExp = new MyAsyncTaskUpdateUserExperience(baseUrl + scriptToExecute);
        updateUserExp.execute(idUser, exp);

        try
        {
            Log.v(toString(), updateUserExp.get());
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

    public void updateUser(String username, String email, String password, String id)
    {
        scriptToExecute = "scripts/updateUser.php";

        MyAsyncTaskUpdateUser updateUser = new MyAsyncTaskUpdateUser(baseUrl + scriptToExecute);
        updateUser.execute(username, email, password, id);
        Log.d(toString(),username+" - "+email+" - "+password+" - pour l'id : "+id);
        try
        {
            Log.v(toString(), updateUser.get());
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
    class MyAsyncTaskUpdateUser extends AsyncTask<String, Void, String>
    {
        String urlProvided = null;

        public MyAsyncTaskUpdateUser(String url)
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
                Log.d(toString()," username: "+String.valueOf(params[0])+
                        " mail: "+String.valueOf(params[1])+
                        " password: "+String.valueOf(params[2])+
                        " -> ID : "+String.valueOf(params[3]));
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", String.valueOf(params[0]))
                        .appendQueryParameter("mail", String.valueOf(params[1]))
                        .appendQueryParameter("password", String.valueOf(params[2]))
                        .appendQueryParameter("idUser", String.valueOf(params[3]));

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



    class MyAsyncTaskUpdateUserExperience extends AsyncTask<Integer, Void, String>
    {

        String urlProvided = null;

        public MyAsyncTaskUpdateUserExperience(String url)
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
                        .appendQueryParameter("exp", String.valueOf(params[1]));

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
            catch (Exception e)
            {

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


}
