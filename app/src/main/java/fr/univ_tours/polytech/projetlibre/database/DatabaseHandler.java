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
    protected String scriptToExecute = null;
    protected final String baseUrl = "http://projetlibrecapocombemorel.netai.net/";

    protected final int TIMEOUT = 3000;

}
