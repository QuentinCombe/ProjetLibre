package fr.univ_tours.polytech.projetlibre.controller;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.database.DatabaseHandler;
import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.Objective;
import fr.univ_tours.polytech.projetlibre.view.MapTab;

/**
 * Created by Alkpo on 14/02/2017.
 */

public class MapController implements View.OnClickListener
{
    private View mRootView;

    private LinearLayout objectiveInfoLayout;
    private ImageView mClueImageView;

    private ArrayList<CircleOptions> mCircles = new ArrayList<>();
    private List<Objective> mListObjectives = null;

    private MapTab mMapTab = null;

    private int mIdCircleSelected = -1;

    private int mCircleColor = 0x750000ff;


    public void MapController()
    {

    }

    public ArrayList<CircleOptions> constructCircles()
    {
        mListObjectives = DatabaseHandler.getInstance().getObjectives();

        if (mListObjectives != null)
        {
            for (Objective objective : mListObjectives)
            {
                CircleOptions circleOptions = Objective.convertToCircleOptions(objective);

                circleOptions.strokeWidth(0.0f).fillColor(mCircleColor);
                mCircles.add(circleOptions);
            }
        }
        else
        {

        }

        return mCircles;

    }

    public void setParameters(MapTab mapTab, View rootView)
    {
        mRootView = rootView;
        mMapTab = mapTab;

        objectiveInfoLayout = (LinearLayout) mRootView.findViewById(R.id.objectiveInfoLayout);
        mClueImageView = (ImageView) mRootView.findViewById(R.id.clueImageView);

        Button showClueButton = (Button) mRootView.findViewById(R.id.showClueButton);

        showClueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClue();
            }
        });

        mClueImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClueImageView.setVisibility(View.INVISIBLE);
            }
        });

        Button openCameraModeButton = (Button) mRootView.findViewById(R.id.openCameraModeButton);
        openCameraModeButton.setOnClickListener(this);

        Button checkObjectiveButton = (Button) mRootView.findViewById(R.id.checkObjective);
        checkObjectiveButton.setOnClickListener(this);

    }

    public int getIdCircleClicked(LatLng position)
    {
        for (int i = 0; i < mCircles.size(); i++)
        {
            LatLng center = mCircles.get(i).getCenter();
            double radius = mCircles.get(i).getRadius();
            float[] distance = new float[1];
            Location.distanceBetween(position.latitude, position.longitude, center.latitude, center.longitude, distance);

            if (distance[0] < radius)
            {
                return i;
            }
        }

        return -1;
    }

    public int handleOnMapClick(LatLng position)
    {
        mIdCircleSelected = getIdCircleClicked(position);

        if (mIdCircleSelected != -1)
        {
            Log.w(getClass().toString(), "Dangereux : utiliser une autre structure de donnee/autre modele");

            Objective objective = mListObjectives.get(mIdCircleSelected);

            objectiveInfoLayout.setVisibility(View.VISIBLE);

            TextView textView = (TextView) objectiveInfoLayout.findViewById(R.id.idCircleTextView);
            textView.setText("Cercle " + mIdCircleSelected);

            Log.v(getClass().toString(), "IdObjective = " + objective.id);

            mClueImageView.setImageBitmap(objective.clue.image);
        }
        else
        {
            if (objectiveInfoLayout.getVisibility() == View.VISIBLE)
            {
                objectiveInfoLayout.setVisibility(View.INVISIBLE);
            }
        }

        mClueImageView.setVisibility(View.INVISIBLE);

        return mIdCircleSelected;
    }

    public void openCameraMode()
    {
        Intent intent = new Intent(mMapTab.getContext(), fr.univ_tours.polytech.projetlibre.unity.UnityPlayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //intent.addFlags(Intent.FLAG)

        mMapTab.startActivityForResult(intent, 0);

    }

    public void clearMapView()
    {
        mClueImageView.setVisibility(View.INVISIBLE);
    }

    public void showClue()
    {
        mClueImageView.setVisibility(View.VISIBLE);
    }

    public void tryToReload()
    {
        mMapTab.zoomOnCurrentLocation();

        mMapTab.tryToGetCircles();
    }

    public void checkIfAnObjectiveWasFound()
    {
        // Try to open the file
        if (mMapTab != null)
        {
            File file = new File(mMapTab.getContext().getExternalFilesDir(null), "obj.txt");
            String contentFile = null;

            if (!file.exists())
            {
                Log.v(toString(), "Nothing found !");
            } else
            {
                try
                {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    contentFile = br.readLine();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }

            System.out.println(file.toString());

            if (contentFile != null)
            {
                Log.v(toString(), "Contenu = " + contentFile);

                showClue();
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.openCameraModeButton)
        {
            Intent intent = new Intent(mMapTab.getContext(), fr.univ_tours.polytech.projetlibre.unity.MyUnityPlayerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            mMapTab.startActivityForResult(intent, 0);
        }
        else if (v.getId() == R.id.checkObjective)
        {
            checkIfAnObjectiveWasFound();
        }
    }
}
