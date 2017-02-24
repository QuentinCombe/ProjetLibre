package fr.univ_tours.polytech.projetlibre;

import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.model.Objective;

/**
 * Created by Alkpo on 14/02/2017.
 */

public class MapController
{
    private View mRootView;

    private LinearLayout objectiveInfoLayout;
    private ImageView mClueImageView;

    private ArrayList<CircleOptions> mCircles = new ArrayList<>();
    private List<Objective> mListObjectives = null;

    private int mIdCircleSelected = -1;

    private GoogleMap.InfoWindowAdapter infoWindow;

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
            Log.v(getClass().toString(), "Cant reach database, add dummy circles");

            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(47.3945427, 0.6910287000000608))
                    .radius(100)
                    .strokeWidth(0.0f)
                    .fillColor(mCircleColor);

            mCircles.add(circleOptions);

            circleOptions = new CircleOptions()
                    .center(new LatLng(47.365197, 0.680741))
                    .radius(100)
                    .strokeWidth(0.0f)
                    .fillColor(mCircleColor);

            mCircles.add(circleOptions);

            circleOptions = new CircleOptions()
                    .center(new LatLng(47.367671, 0.684174))
                    .radius(100)
                    .strokeWidth(0.0f)
                    .fillColor(mCircleColor);

            mCircles.add(circleOptions);
        }

        return mCircles;

    }

    public void setRootView(View rootView)
    {
        mRootView = rootView;

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

    public void clearMapView()
    {
        mClueImageView.setVisibility(View.INVISIBLE);
    }

    public void showClue()
    {
        mClueImageView.setVisibility(View.VISIBLE);
    }

}
