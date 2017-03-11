package fr.univ_tours.polytech.projetlibre.view;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import fr.univ_tours.polytech.projetlibre.controller.MapController;
import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.controller.MainActivity;
import fr.univ_tours.polytech.projetlibre.model.Objective;

public class MapTab extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private GoogleMap mMap = null;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private View mRootView;

    private MapController mMapController = null;

    private HashMap<Objective, Circle> mCircles = new HashMap<>();
    private Circle mCircledSelected = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        MainActivity mainActivity = (MainActivity) getActivity();

        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapController = mainActivity.getmMapController();
        mMapController.setParameters(this, mRootView);

        // In MapController.java ??
        MapView mapView = (MapView) mRootView.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(this);


        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        return mRootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Log.v("MapTab", "onMapReady");

        mMap = googleMap;

        MapsInitializer.initialize(getActivity());

        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        zoomOnCurrentLocation();

        tryToGetCircles();

    }

    public void zoomOnCurrentLocation()
    {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15.0f));
        } else
        {
            Toast.makeText(this.getContext(), "Can't find your location...", Toast.LENGTH_LONG).show();
        }
    }

    public void tryToGetCircles()
    {
        if (mCircles.size() == 0)
        {
            HashMap<Objective, CircleOptions> allCircles = mMapController.constructCircles();

            if (allCircles.size() > 0)
            {
                for (Objective objective : allCircles.keySet())
                {
                    mCircles.put(objective, mMap.addCircle(allCircles.get(objective)));

                    //mCircles.add(mMap.addCircle(circleOption));
                }
            } else
            {
                Toast.makeText(this.getContext(), "Can't reach database...", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateCircle(Objective objective)
    {
        mCircles.get(objective).setFillColor(0xff000000);

        /*if (mCircles.size() == 0)
        {
            ArrayList<CircleOptions> circleOptions = mMapController.constructCircles();

            if (circleOptions.size() > 0)
            {
                for (CircleOptions circleOption : circleOptions)
                {
                    mCircles.add(mMap.addCircle(circleOption));
                }
            } else
            {
                Toast.makeText(this.getContext(), "Can't reach database...", Toast.LENGTH_LONG).show();
            }
        }*/
    }

    @Override
    public void onMapClick(LatLng position)
    {
        int idCircledClicked = mMapController.handleOnMapClick(position);

        if (mCircledSelected != null)
        {
            mCircledSelected.setStrokeWidth(0.0f);
            mCircledSelected = null;
        }

        if (idCircledClicked != -1)
        {
            mCircledSelected = mCircles.get(idCircledClicked);

            mCircledSelected.setStrokeWidth(8.0f);

            mMap.animateCamera(CameraUpdateFactory.newLatLng(position));
        }
    }

    public void openCameraMode(View v)
    {
        mMapController.openCameraMode();
    }

    public void showClue(View view)
    {
        mMapController.showClue();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        mMapController.checkIfAnObjectiveWasFound();
    }

    public void onStart()
    {
        mGoogleApiClient.connect();

        super.onStart();
    }

    public void onStop()
    {
        mGoogleApiClient.disconnect();

        super.onStop();
    }


    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }


}
