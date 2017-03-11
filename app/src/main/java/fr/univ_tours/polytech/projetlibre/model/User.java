package fr.univ_tours.polytech.projetlibre.model;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.database.DatabaseHandler;

/**
 * Created by quent on 06/03/2017.
 */

public class User implements Serializable {

    public int idUser;
    public String username;
    public String mail;
    public String password;
    public int exp;
    public int lvl;
    public Bitmap profilePicture;

    public ArrayList<Objective> achievedObjectives = new ArrayList<>();

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

            user.achievedObjectives =  DatabaseHandler.getInstance().getAchievedObjectivesById(user.idUser);

            for (Objective achievedObjective : user.achievedObjectives)
            {
                Log.v("Objectifs trouve : ", "Id = " + achievedObjective.id);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return user;

    }
}
