package fr.univ_tours.polytech.projetlibre.model;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import fr.univ_tours.polytech.projetlibre.database.ClueDB;

/**
 * Created by Alkpo on 23/02/2017.
 */

public class Clue implements Serializable
{
    public int idClue;
    private String imageName;
    private boolean imageLoaded = false;
    public Bitmap image;

    public void loadImage()
    {
        this.image = ClueDB.getInstance().getBitmapClueFromName(imageName);

        imageLoaded = true;
    }

    public boolean isImageLoaded()
    {
        return imageLoaded;
    }

    public static Clue convertFromJsonObject(JSONObject jsonObject)
    {
        Clue clue = new Clue();

        try
        {
            clue.idClue = jsonObject.getInt("idClue");

            clue.imageName = jsonObject.getString("image");
            clue.image = null;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return clue;

    }




}
