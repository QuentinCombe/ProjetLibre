package fr.univ_tours.polytech.projetlibre.controller;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.User;

/**
 * Created by Alkpo on 27/02/2017.
 */
public class ProfileController
{
    private View mRootView;
    private User user;

    /*Affecter les valeur au component de la vue
    Tout ce ue j'ai fait dans la vue
     *  */

    public ProfileController(User user){
        this.user = user;
    }

    public void setRootView(View rootView)
    {
        mRootView = rootView;
        Log.d(toString(),"USER RECIEVED"+user.username);

        TextView profilName = (TextView) rootView.findViewById(R.id.ProfileName);
        profilName.setText(user.username);

        TextView lvl = (TextView) rootView.findViewById(R.id.lvlField);
        lvl.setText("Lvl :"+user.lvl);

        TextView exp = (TextView) rootView.findViewById(R.id.ExpField);
        exp.setText("Experience :"+user.exp);


    }
}
