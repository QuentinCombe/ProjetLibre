package fr.univ_tours.polytech.projetlibre;

import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alkpo on 14/02/2017.
 */

public class MapController
{
    private View mRootView;

    private LinearLayout objectiveInfoLayout;
    private ImageView mClueImageView;

    private ArrayList<CircleOptions> circles = new ArrayList<>();

    private GoogleMap.InfoWindowAdapter infoWindow;

    public void MapController()
    {

    }

    public ArrayList<CircleOptions> constructCircles()
    {
        // Recherche dans la BDD etc

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(47.3945427, 0.6910287000000608))
                .radius(100)
                .strokeWidth(0.0f)
                .fillColor(0x750000ff);


        circles.add(circleOptions);

        circleOptions = new CircleOptions()
                .center(new LatLng(47.365197, 0.680741))
                .radius(100)
                .strokeWidth(0.0f)
                .fillColor(0x750000ff);

        circles.add(circleOptions);

        circleOptions = new CircleOptions()
                .center(new LatLng(47.367671, 0.684174))
                .radius(100)
                .strokeWidth(0.0f)
                .fillColor(0x750000ff);

        circles.add(circleOptions);

        return circles;

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
        for (int i = 0; i < circles.size(); i++)
        {
            LatLng center = circles.get(i).getCenter();
            double radius = circles.get(i).getRadius();
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
        int idCircleClicked = getIdCircleClicked(position);

        if (idCircleClicked != -1)
        {
            objectiveInfoLayout.setVisibility(View.VISIBLE);


            TextView textView = (TextView) objectiveInfoLayout.findViewById(R.id.idCircleTextView);
            textView.setText("Cercle " + idCircleClicked);
        }
        else
        {
            if (objectiveInfoLayout.getVisibility() == View.VISIBLE)
            {
                objectiveInfoLayout.setVisibility(View.INVISIBLE);
            }
        }

        return idCircleClicked;
    }

    public void showClue()
    {
        mClueImageView.setVisibility(View.VISIBLE);
    }

}
