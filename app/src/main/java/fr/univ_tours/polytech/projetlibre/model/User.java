package fr.univ_tours.polytech.projetlibre.model;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import fr.univ_tours.polytech.projetlibre.database.DatabaseHandler;

/**
 * Created by quent on 06/03/2017.
 */

public class User {

    public int idUser;
    public String username;
    public String mail;
    public String password;
    public int exp;
    public int lvl;
    public Bitmap profilePicture;


    public static User convertFromJsonObject(JSONObject jsonObject){

        User user = new User();

        try
        {
            user.idUser = jsonObject.getInt("idUser");
            user.username = jsonObject.getString("username");
            user.mail = jsonObject.getString("mail");
            user.password = jsonObject.getString("password");
            user.exp = jsonObject.getInt("exp");
            user.lvl = jsonObject.getInt("level");
           // user.profilePicture = DatabaseHandler.getInstance().getBitmapClueFromName(jsonObject.getString("image"));

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return user;

    }
}
