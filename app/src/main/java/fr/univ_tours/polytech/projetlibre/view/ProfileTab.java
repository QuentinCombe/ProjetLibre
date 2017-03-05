package fr.univ_tours.polytech.projetlibre.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.controller.MainActivity;
import fr.univ_tours.polytech.projetlibre.controller.ProfileController;


public class ProfileTab extends Fragment
{
    private ProfileController mProfileController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mProfileController = mainActivity.getmProfileController();

        mProfileController.setRootView(rootView);
<<<<<<< HEAD
=======

>>>>>>> c6cfdc7674bffb9177d6b05aa53d1ad566548d29
        return rootView;
    }
}
