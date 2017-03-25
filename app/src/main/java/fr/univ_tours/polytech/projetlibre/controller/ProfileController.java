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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.GlobalDatas;
import fr.univ_tours.polytech.projetlibre.model.Objective;
import fr.univ_tours.polytech.projetlibre.model.ObjectiveFoundDataModel;
import fr.univ_tours.polytech.projetlibre.model.User;

/**
 * Created by Alkpo on 27/02/2017.
 */
public class ProfileController implements View.OnClickListener
{
    private View mRootView;

    private MainActivity mMainActivity = null;

    private ListView achievedObjectivesListView = null;
    private ArrayAdapter<ObjectiveFoundDataModel> adapter;
    private ArrayList<ObjectiveFoundDataModel> listObjectivesDataModel = null;

    private TextView playerNameTextView = null;
    private TextView expTextView = null;


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

        TextView lvl = (TextView) mRootView.findViewById(R.id.lvlField);
        lvl.setText("Lvl :"+user.lvl);

        expTextView = (TextView) mRootView.findViewById(R.id.ExpField);
        expTextView.setText("Experience : " + user.exp);

        Button deconnexionButton = (Button) mRootView.findViewById(R.id.deconnexionButton);
        deconnexionButton.setOnClickListener(this);

        achievedObjectivesListView = (ListView) mRootView.findViewById(R.id.achievedObjectivesList);

        playerNameTextView = (TextView) mRootView.findViewById(R.id.playerNameTextView);
        playerNameTextView.setText(user.username);

        constructAchievedObjectivesList();


    }

    private void constructAchievedObjectivesList()
    {
        listObjectivesDataModel = new ArrayList<>();
        List<Objective> achievedObjectives = GlobalDatas.getInstance().mCurrentUser.achievedObjectives;

        for (Objective objective : achievedObjectives)
        {
            listObjectivesDataModel.add(new ObjectiveFoundDataModel(String.valueOf(objective.id), objective.textAfterDiscovery, "Trouve ici"));
        }

        adapter = new ObjectiveListViewAdapter(listObjectivesDataModel, mMainActivity);

        achievedObjectivesListView.setAdapter(adapter);
    }

    public void updateObjectiveFound(Objective objectiveFound)
    {
        listObjectivesDataModel.add(new ObjectiveFoundDataModel(String.valueOf(objectiveFound.id), objectiveFound.textAfterDiscovery, "Trouve ici"));

        adapter.notifyDataSetChanged();

        expTextView.setText("Experience : " + GlobalDatas.getInstance().mCurrentUser.exp);
    }

    public void updatePlayerName(String name)
    {
        playerNameTextView.setText(name);
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
