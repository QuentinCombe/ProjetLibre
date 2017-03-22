package fr.univ_tours.polytech.projetlibre.controller;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.vuforia.VIEW;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.database.AchievedObjectivesDB;
import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.GlobalDatas;
import fr.univ_tours.polytech.projetlibre.model.Objective;
import fr.univ_tours.polytech.projetlibre.model.User;
import fr.univ_tours.polytech.projetlibre.view.ClueView;
import fr.univ_tours.polytech.projetlibre.view.ObjectiveFoundView;

/**
 * Created by Alkpo on 14/02/2017.
 */

public class MapController implements View.OnClickListener
{
    private View mRootView;

    private LinearLayout objectiveInfoLayout;

    private Button openCameraButton;

    private HashMap<Objective, CircleOptions> mCirclesOptions = new HashMap<>();
    private HashMap<Objective, Circle> mCircles = new HashMap<>();
    private Circle mSelectedCircle = null;
    private Objective mSelectedObjective = null;

    private ObjectiveFoundView objectiveFoundView = null;
    private ClueView mClueView = null;

    private View cantClickView = null;

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
                } else
                {
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

        Button showClueButton = (Button) mRootView.findViewById(R.id.showClueButton);

        showClueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!mSelectedObjective.clue.isImageLoaded())
                {
                    Toast.makeText(mMainActivity.getApplicationContext(), "Récupération de l'indice en cours...", Toast.LENGTH_SHORT).show();
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showClue();
                    }
                }, 100);

            }
        });


        Button openCameraModeButton = (Button) mRootView.findViewById(R.id.openCameraModeButton);
        openCameraModeButton.setOnClickListener(this);



        openCameraButton = (Button) mRootView.findViewById(R.id.openCameraModeButton);

        cantClickView = mRootView.findViewById(R.id.cantClickView);

        objectiveFoundView = (ObjectiveFoundView) mRootView.findViewById(R.id.objectiveFoundView);
        objectiveFoundView.setCantClickView(cantClickView);

        mClueView = (ClueView) mRootView.findViewById(R.id.clueView);
        mClueView.setCantClickView(cantClickView);

        mClueView.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            mClueView.setVisibility(View.INVISIBLE);
            cantClickView.setVisibility(View.INVISIBLE);

            // mMap.setClic
        }
    });
    }

    public void setMap(GoogleMap map)
    {
        this.mMap = map;
    }

    public Circle getSelectedCircle(LatLng position)
    {
        for (Circle circle : mCircles.values())
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

    public void handleOnMapClick(LatLng position)
    {

        Circle circle = getSelectedCircle(position);

        // Si on a déjà cliqué sur un autre cercle au préalable
        if (mSelectedCircle != null)
        {
            mSelectedCircle.setStrokeWidth(0.0f);
            mSelectedCircle = null;
            mSelectedObjective = null;

        }

        if (circle != null && circle != mSelectedCircle)
        {
            mSelectedObjective = null;

            for (Objective anObjective : mCircles.keySet())
            {
                if (mCircles.get(anObjective) == circle)
                {
                    mSelectedObjective = anObjective;
                    break;
                }
            }

            if (mSelectedObjective != null)
            {
                objectiveInfoLayout.setVisibility(View.VISIBLE);

                Log.v(getClass().toString(), "IdObjective = " + mSelectedObjective.id);

                if (mSelectedObjective.clue.image != null)
                {
                    mClueView.setClue(mSelectedObjective.clue);
                }

                if (GlobalDatas.getInstance().mCurrentUser.achievedObjectives.contains(mSelectedObjective))
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

        } else
        {
            if (objectiveInfoLayout.getVisibility() == View.VISIBLE)
            {
                objectiveInfoLayout.setVisibility(View.INVISIBLE);
            }
        }

        mClueView.setVisibility(View.INVISIBLE);

    }

    public void clearMapView()
    {
        mClueView.setVisibility(View.INVISIBLE);
    }

    public void showClue()
    {
        if (!mSelectedObjective.clue.isImageLoaded())
        {
            mSelectedObjective.clue.loadImage();

            mClueView.setClue(mSelectedObjective.clue);


        }

        cantClickView.setVisibility(View.VISIBLE);
        //cantClickView.bringToFront();

        mClueView.setVisibility(View.VISIBLE);
        //mClueImageView.bringToFront();

    }

    public void checkIfAnObjectiveWasFound()
    {
        File file = new File(mMainActivity.getExternalFilesDir(null), "obj.txt");
        String contentFile = null;

        if (!file.exists())
        {
            Toast.makeText(mMainActivity, "Aucun objectif trouve", Toast.LENGTH_LONG).show();
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

        if (contentFile != null)
        {
            User currentUser = GlobalDatas.getInstance().mCurrentUser;

            Objective objectiveFound = GlobalDatas.getInstance().getObjectiveById(Integer.parseInt(contentFile));

            if (!currentUser.achievedObjectives.contains(objectiveFound))
            {
                currentUser.addObjectiveFound(objectiveFound);

                file.delete();

                // Update the color of the circle
                mCircles.get(objectiveFound).setFillColor(ContextCompat.getColor(mMainActivity, R.color.foundCircle));

                mMainActivity.updateObjectiveFound(objectiveFound);

                createObjectiveFoundView(objectiveFound);
            } else
            {
                Toast.makeText(mMainActivity, "Vous avez deja trouve cet objectif", Toast.LENGTH_LONG).show();
            }


        }


    }

    private void createObjectiveFoundView(Objective objectiveFound)
    {
        objectiveFoundView.setObjective(objectiveFound);

        objectiveFoundView.setVisibility(View.VISIBLE);
        cantClickView.setVisibility(View.VISIBLE);
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
