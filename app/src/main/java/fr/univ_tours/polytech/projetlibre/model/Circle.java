package fr.univ_tours.polytech.projetlibre.model;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Alkpo on 14/02/2017.
 */

public class Circle
{
    private double mLatitude;
    private double mLongitude;
    private double mRadius;

    public Circle(double latitude, double longitude, double radius)
    {
        mLatitude = latitude;
        mLongitude = longitude;
        mRadius = radius;
    }

    public double getLatitude()
    {
        return mLatitude;
    }

    public double getLongitude()
    {
        return mLongitude;
    }

    public double getRadius()
    {
        return mRadius;
    }


}
