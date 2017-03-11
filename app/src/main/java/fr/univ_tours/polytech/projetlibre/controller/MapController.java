package fr.univ_tours.polytech.projetlibre.controller;

import android.content.Intent;
import android.location.Location;
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
import java.util.HashMap;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.database.DatabaseHandler;
import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.GlobalDatas;
import fr.univ_tours.polytech.projetlibre.model.Objective;
import fr.univ_tours.polytech.projetlibre.model.User;
import fr.univ_tours.polytech.projetlibre.view.MapTab;

/**
 * Created by Alkpo on 14/02/2017.
 */

public class MapController implements View.OnClickListener
{
    private View mRootView;

    private LinearLayout objectiveInfoLayout;
    private ImageView mClueImageView;

    private HashMap<Objective, CircleOptions> mCircles = new HashMap<>();

    private MapTab mMapTab = null;

    private int mIdCircleSelected = -1;

    private int mCircleColor = 0x750000ff;
    private int mCircleColor2 = 0xff000000;

    private User mUser;


    public MapController(User user)
    {
        mUser = user;
    }

    public HashMap<Objective, CircleOptions> constructCircles()
    {
        List<Objective> allObjectives = GlobalDatas.getInstance().allObjectives;

        if (allObjectives != null)
        {
            for (Objective objective : allObjectives)
            {
                CircleOptions circleOptions = Objective.convertToCircleOptions(objective);

                if (!mUser.achievedObjectives.contains(objective))
                {
                    circleOptions.strokeWidth(0.0f).fillColor(mCircleColor);
                }
                else
                {
                    Log.v(toString(), "CERCLE TROUVEEEE");
                    circleOptions.strokeWidth(0.0f).fillColor(mCircleColor2);
                }



                mCircles.put(objective, circleOptions);
            }
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

        showClueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showClue();
            }
        });

        mClueImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mClueImageView.setVisibility(View.INVISIBLE);
            }
        });


        Button openCameraModeButton = (Button) mRootView.findViewById(R.id.openCameraModeButton);
        openCameraModeButton.setOnClickListener(this);
    }

    public CircleOptions getCircleSelected(LatLng position)
    {
        for (CircleOptions circleOptions : mCircles.values())
        {
            LatLng center = circleOptions.getCenter();
            double radius = circleOptions.getRadius();
            float[] distance = new float[1];
            Location.distanceBetween(position.latitude, position.longitude, center.latitude, center.longitude, distance);

            if (distance[0] < radius)
            {
                return circleOptions;
            }
        }

        return null;
    }

    public int handleOnMapClick(LatLng position)
    {
        CircleOptions circleSelected = getCircleSelected(position);

        if (circleSelected != null)
        {
            Objective objective = null;

            for (Objective anObjective : mCircles.keySet())
            {
                if (mCircles.get(anObjective) == circleSelected)
                {
                    objective = anObjective;
                    break;
                }
            }

            if (objective != null)
            {
                objectiveInfoLayout.setVisibility(View.VISIBLE);

                Log.v(getClass().toString(), "IdObjective = " + objective.id);

                mClueImageView.setImageBitmap(objective.clue.image);
            }
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
                Toast.makeText(mMapTab.getContext(), "Aucun objectif trouve", Toast.LENGTH_LONG).show();
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
                Objective objectiveFound = GlobalDatas.getInstance().getObjectiveById(Integer.parseInt(contentFile));

                Log.v(toString(), "Contenu = " + contentFile);

                Toast.makeText(mMapTab.getContext(), "Vous avez trouve le num " + objectiveFound.id, Toast.LENGTH_LONG).show();

                DatabaseHandler.getInstance().insertAchievedObjective(mUser.idUser, Integer.parseInt(contentFile));

                //TODO Remove the file
                boolean d0 = file.delete();
                Log.w("Delete Check", "File deleted: " + d0);

                mMapTab.updateCircle(objectiveFound);
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
    }
}
