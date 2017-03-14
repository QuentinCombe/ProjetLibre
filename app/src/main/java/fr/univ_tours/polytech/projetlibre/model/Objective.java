package fr.univ_tours.polytech.projetlibre.model;

import android.util.Log;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.database.DatabaseHandler;

/**
 * Created by Alkpo on 14/02/2017.
 */

public class Objective implements Serializable
{
    public int id;
    public int difficulty;
    public Circle circle;
    public String textAfterDiscovery;

    public Clue clue;

    public static List<Objective> convertFromJsonArray(JSONArray array)
    {
        List<Objective> listObjectives = new ArrayList<>();

        for (int i = 0; i < array.length(); i++)
        {
            Objective objective = new Objective();

            try
            {
                JSONObject objectiveJsonObject = array.getJSONObject(i);

                Log.v("convertFromJsonArray", objectiveJsonObject.toString());
                Log.v("convertFromJsonArray", "Le clue quon veut est " + objectiveJsonObject.getInt("idClue"));

                objective.id = objectiveJsonObject.getInt("idObjective");
                objective.circle =
                        new Circle(objectiveJsonObject.getDouble("latitude"),
                                objectiveJsonObject.getDouble("longitude"),
                                objectiveJsonObject.getDouble("radius"));

                objective.clue = DatabaseHandler.getInstance().getClueFromId(objectiveJsonObject.getInt("idClue"));

                objective.textAfterDiscovery = objectiveJsonObject.getString("textAfterDiscovery");


                listObjectives.add(objective);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        if (listObjectives.size() == 0)
        {
            System.out.println("Didnt get the objectives properly...");

            return null;
        } else
        {
            return listObjectives;
        }
    }

    public static ArrayList<Objective>  convertListAchievedObjectivesFromJSONArray(JSONArray array)
    {
        ArrayList<Objective> listObjectives = new ArrayList<>();

        for (int i = 0; i < array.length(); i++)
        {
            try
            {
                JSONObject objectiveJsonObject = array.getJSONObject(i);

                int idObjective = objectiveJsonObject.getInt("idObjective");

                listObjectives.add(GlobalDatas.getInstance().getObjectiveById(idObjective));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

       return listObjectives;
    }

    public static CircleOptions convertToCircleOptions(Objective objective)
    {
        Circle circle = objective.circle;

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(circle.getLatitude(), circle.getLongitude()))
                .radius(circle.getRadius());

        return circleOptions;
    }

}
