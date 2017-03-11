package fr.univ_tours.polytech.projetlibre.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.controller.MainActivity;
import fr.univ_tours.polytech.projetlibre.controller.SettingsController;


public class SettingsTab extends Fragment {

    private SettingsController mSettingsController;
    private static final int SELECT_PICTURE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView =  inflater.inflate(R.layout.fragment_settings, container, false);
        TextView updatePicture = (TextView) rootView.findViewById(R.id.EditProfil);
        updatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });
        MainActivity mainActivity = (MainActivity) getActivity();
        mSettingsController = mainActivity.getmSettingsController();
        mSettingsController.setRootView(rootView);

        
        return rootView;
    }


}
