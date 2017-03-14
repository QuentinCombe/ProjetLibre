package fr.univ_tours.polytech.projetlibre.controller;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
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
import fr.univ_tours.polytech.projetlibre.view.ObjectiveFoundView;

/**
 * Created by Alkpo on 14/02/2017.
 */

public class MapController implements View.OnClickListener
{
    private View mRootView;

    private FrameLayout mapFragmentLayout;

    private LinearLayout objectiveInfoLayout;

    private ImageView mClueImageView;
    private Button openCameraButton;

    private HashMap<Objective, CircleOptions> mCirclesOptions = new HashMap<>();
    private HashMap<Objective, Circle> mCircles = new HashMap<>();
    private Circle mSelectedCircle = null;

    private ObjectiveFoundView objectiveFoundView = null;

    private MainActivity mMainActivity = null;
    private GoogleMap mMap = null;


    public MapController(MainActivity mainActivity)
    {
        mMainActivity = mainActivity;
    }

    public HashMap<Objective, CircleOptions> constructCircles()
    {
        List<Objective> allObjectives = GlobalDatas.getInstance().allObjectives;
        List<Objective> achievedObjectives = GlobalDatas.getInstance().mCurrentUser.achievedObjectives;

        if (allObjectives != null)
        {
            for (Objective objective : allObjectives)
            {
                CircleOptions circleOptions = Objective.convertToCircleOptions(objective);

                if (!achievedObjectives.contains(objective))
                {
                    circleOptions.strokeWidth(0.0f).fillColor(ContextCompat.getColor(mMainActivity, R.color.normalCircle));
                }
                else
                {
                    Log.v(toString(), "CERCLE TROUVEEEE");
                    circleOptions.strokeWidth(0.0f).fillColor(ContextCompat.getColor(mMainActivity, R.color.foundCircle));
                }

                Circle circle = mMap.addCircle(circleOptions);

                mCirclesOptions.put(objective, circleOptions);

                mCircles.put(objective, circle);
            }
        }

        return null;

    }

    public void setRootView(View rootView)
    {
        mRootView = rootView;

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

        objectiveFoundView = (ObjectiveFoundView) mRootView.findViewById(R.id.objectiveFoundView);

        openCameraButton = (Button) mRootView.findViewById(R.id.openCameraModeButton);
    }

    public void setMap(GoogleMap map)
    {
        this.mMap = map;
    }

    public Circle getSelectedCircle(LatLng position)
    {
        for (Circle circle: mCircles.values())
        {
            LatLng center = circle.getCenter();
            double radius = circle.getRadius();
            float[] distance = new float[1];
            Location.distanceBetween(position.latitude, position.longitude, center.latitude, center.longitude, distance);

            if (distance[0] < radius)
            {
                return circle;
            }
        }

        return null;
    }

    public int handleOnMapClick(LatLng position)
    {
        Circle circle = getSelectedCircle(position);

        // Si on a déjà cliqué sur un autre cercle au préalable
        if (mSelectedCircle != null)
        {
            mSelectedCircle.setStrokeWidth(0.0f);
            mSelectedCircle = null;
        }

        if (circle != null)
        {
            Objective objective = null;

            for (Objective anObjective : mCircles.keySet())
            {
                if (mCircles.get(anObjective) == circle)
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

                if (GlobalDatas.getInstance().mCurrentUser.achievedObjectives.contains(objective))
                {
                    openCameraButton.setActivated(false);
                }
                else
                {
                    openCameraButton.setActivated(true);
                }
            }


            mSelectedCircle = circle;
            mSelectedCircle.setStrokeWidth(8.0f);

            mMap.animateCamera(CameraUpdateFactory.newLatLng(position));

        }
        else
        {
            if (objectiveInfoLayout.getVisibility() == View.VISIBLE)
            {
                objectiveInfoLayout.setVisibility(View.INVISIBLE);
            }
        }

        mClueImageView.setVisibility(View.INVISIBLE);

        return 1;
    }

    public void clearMapView()
    {
        mClueImageView.setVisibility(View.INVISIBLE);
    }

    public void showClue()
    {
        mClueImageView.setVisibility(View.VISIBLE);
    }

    public void checkIfAnObjectiveWasFound()
    {
        // Try to open the file
        //if (mMapTab != null)
        {
            File file = new File(mMainActivity.getExternalFilesDir(null), "obj.txt");
            String contentFile = null;

            if (!file.exists())
            {
                Toast.makeText(mMainActivity, "Aucun objectif trouve", Toast.LENGTH_LONG).show();
            }
            else
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

            if (contentFile != null)
            {
                User currentUser = GlobalDatas.getInstance().mCurrentUser;

                Objective objectiveFound = GlobalDatas.getInstance().getObjectiveById(Integer.parseInt(contentFile));

                if (!currentUser.achievedObjectives.contains(objectiveFound))
                {
                    Toast.makeText(mMainActivity, "Vous avez trouve le num " + objectiveFound.id, Toast.LENGTH_LONG).show();

                    DatabaseHandler.getInstance().insertAchievedObjective(currentUser.idUser, Integer.parseInt(contentFile));
                    currentUser.achievedObjectives.add(objectiveFound);

                    file.delete();

                    // Update the color of the circle
                    mCircles.get(objectiveFound).setFillColor(ContextCompat.getColor(mMainActivity, R.color.foundCircle));
                }

                mMainActivity.updateObjectiveFound(objectiveFound);

                createObjectiveFoundView(objectiveFound);
            }
            else
            {
                Toast.makeText(mMainActivity, "Vous avez deja trouve cet objectif", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void createObjectiveFoundView(Objective objectiveFound)
    {
        /*ObjectiveFoundView objectiveFoundView = new ObjectiveFoundView(this.mMainActivity.getApplicationContext());
        objectiveFoundView.setObjective(objectiveFound);

        mRootView.add(objectiveFoundView);*/

        objectiveFoundView.setObjective(objectiveFound);

        objectiveFoundView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.openCameraModeButton)
        {
            Intent intent = new Intent(mMainActivity, fr.univ_tours.polytech.projetlibre.unity.MyUnityPlayerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            mMainActivity.startActivityForResult(intent, 0);
        }
    }


}
