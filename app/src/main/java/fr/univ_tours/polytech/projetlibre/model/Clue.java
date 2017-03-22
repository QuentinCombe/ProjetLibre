package fr.univ_tours.polytech.projetlibre.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;

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
    public String description;
    private boolean imageLoaded = false;
    public Bitmap image;

    public void loadImage()
    {

        //rotatedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);

        this.image = ClueDB.getInstance().getBitmapClueFromName(imageName);
        /*Bitmap baseBitmap = ClueDB.getInstance().getBitmapClueFromName(imageName);

        atrix matrix = new Matrix();
        matrix.postRotate(90);

        this.image = Bitmap.createBitmap(baseBitmap, 0, 0, baseBitmap.getWidth(), baseBitmap.getHeight(), matrix, true);*/

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

            clue.description = jsonObject.getString("description");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return clue;

    }




}
