package fr.univ_tours.polytech.projetlibre.model;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.DatabaseHandler;

/**
 * Created by Alkpo on 14/02/2017.
 */

public class Objective
{
    public int id;
    public int difficulty;
    public Circle circle;

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

                objective.id = objectiveJsonObject.getInt("idObjective");
                objective.circle =
                        new Circle(objectiveJsonObject.getDouble("latitude"),
                                   objectiveJsonObject.getDouble("longitude"),
                                   objectiveJsonObject.getDouble("radius"));

                objective.clue = DatabaseHandler.getInstance().getClueFromId(objectiveJsonObject.getInt("idClue"));

                listObjectives.add(objective);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        if (listObjectives.size() == 0)
        {
            System.out.println("Didnt get the objectives properly...");

            return null;
        }
        else
        {
            return listObjectives;
        }
    }

    public static CircleOptions convertToCircleOptions(Objective objective)
    {
        Circle circle = objective.circle;

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(circle.getLatitude(), circle.getLongitude()))
                .radius(circle.getRadius());

        return circleOptions;
    }

    public static Objective findObjectiveById(List<Objective> listObjectives, int idObjective)
    {
        for (int i = 0; i < listObjectives.size(); i++)
        {
            if (listObjectives.get(i).id == idObjective)
            {
                return listObjectives.get(i);
            }
        }

        return null;
    }

}
