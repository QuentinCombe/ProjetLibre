package fr.univ_tours.polytech.projetlibre.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import fr.univ_tours.polytech.projetlibre.DatabaseHandler;

/**
 * Created by Alkpo on 23/02/2017.
 */

public class Clue
{
    public int idClue;
    public Bitmap image;

    public static Clue convertFromJsonObject(JSONObject jsonObject)
    {
        Clue clue = new Clue();

        try
        {
            clue.idClue = jsonObject.getInt("idClue");

            clue.image = DatabaseHandler.getInstance().getBitmapClueFromName(jsonObject.getString("image"));

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return clue;

    }


}
