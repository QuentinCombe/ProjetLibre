package fr.univ_tours.polytech.projetlibre.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.GlobalDatas;
import fr.univ_tours.polytech.projetlibre.model.User;

/**
 * Created by Alkpo on 27/02/2017.
 */
public class ProfileController implements View.OnClickListener
{
    private View mRootView;
    private MainActivity mMainActivity = null;

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


    }

    @Override
    public void onClick(View v) {

    }
}
