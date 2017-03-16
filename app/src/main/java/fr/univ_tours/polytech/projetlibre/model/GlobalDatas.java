package fr.univ_tours.polytech.projetlibre.model;

import android.util.Log;

import java.util.List;

import fr.univ_tours.polytech.projetlibre.database.ObjectiveDB;

/**
 * Created by Alkpo on 11/03/2017.
 */

public class GlobalDatas
{
    private static GlobalDatas Inst = new GlobalDatas();

    public List<Objective> allObjectives = null;
    public User mCurrentUser = null;


    private GlobalDatas()
    {
        Log.v(toString(), "GlobalDatas constructor");
        allObjectives = ObjectiveDB.getInstance().getObjectives();

        Log.v(toString(), "Tous les objectifs = " + allObjectives.size());
    }

    public static GlobalDatas getInstance()
    {
        return Inst;
    }

    public Objective getObjectiveById(int idObjective)
    {
        Objective objective = null;

        for (Objective itObjective : allObjectives)
        {
            if (itObjective.id == idObjective)
            {
                objective = itObjective;
                break;
            }
        }

        return objective;
    }

    public void setCurrentUser(User user)
    {
        mCurrentUser = user;
    }

}
