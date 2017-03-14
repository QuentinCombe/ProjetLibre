package fr.univ_tours.polytech.projetlibre.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.GlobalDatas;
import fr.univ_tours.polytech.projetlibre.model.Objective;
import fr.univ_tours.polytech.projetlibre.model.User;

/**
 * Created by Alkpo on 27/02/2017.
 */
public class ProfileController implements View.OnClickListener
{
    private View mRootView;

    private MainActivity mMainActivity = null;

    private ListView achievedObjectivesListView = null;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listObjectivesString = null;

    /*Affecter les valeur au component de la vue
    Tout ce ue j'ai fait dans la vue
     *  */

    public ProfileController(MainActivity mainActivity)
    {
        mMainActivity = mainActivity;
    }

    public void setRootView(View rootView)
    {
        mRootView = rootView;
        User user = GlobalDatas.getInstance().mCurrentUser;

        Log.d(toString(),"USER RECIEVED"+user.username);

        TextView profilName = (TextView) mRootView.findViewById(R.id.ProfileName);
        profilName.setText(user.username);

        TextView lvl = (TextView) mRootView.findViewById(R.id.lvlField);
        lvl.setText("Lvl :"+user.lvl);

        TextView exp = (TextView) mRootView.findViewById(R.id.ExpField);
        exp.setText("Experience :"+user.exp);

        Button deconnexionButton = (Button) mRootView.findViewById(R.id.deconnexionButton);
        deconnexionButton.setOnClickListener(this);

        achievedObjectivesListView = (ListView) mRootView.findViewById(R.id.achievedObjectivesList);

        constructAchievedObjectivesList();


    }

    private void constructAchievedObjectivesList()
    {
        listObjectivesString = new ArrayList<>();
        List<Objective> achievedObjectives = GlobalDatas.getInstance().mCurrentUser.achievedObjectives;

        for (Objective objective : achievedObjectives)
        {
            listObjectivesString.add("Num = " + objective.id + " : " + objective.textAfterDiscovery);
        }

        adapter = new ArrayAdapter<String>(
                mMainActivity.getApplicationContext(),
                android.R.layout.simple_list_item_1,
                listObjectivesString);

        achievedObjectivesListView.setAdapter(adapter);
    }

    public void updateObjectiveFound(Objective objectiveFound)
    {
        listObjectivesString.add("Num = " + objectiveFound.id + " : " + objectiveFound.textAfterDiscovery);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.deconnexionButton)
        {
            Intent intent = new Intent(mMainActivity.getApplicationContext(), LoginActivity.class);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mMainActivity.getApplicationContext()).edit();

            editor.remove("userMail");
            editor.remove("userPassword");

            editor.commit();

            mMainActivity.startActivity(intent);
        }
    }
}
