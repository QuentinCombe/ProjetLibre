package fr.univ_tours.polytech.projetlibre.model;

/**
 * Created by Alkpo on 22/03/2017.
 */

public class ObjectiveFoundDataModel
{
    private String id;
    private String afterDiscovery;
    private String location;

    public ObjectiveFoundDataModel(String id, String afterDiscovery, String location)
    {
        this.id = id;
        this.afterDiscovery = afterDiscovery;
        this.location = location;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }



    public String getAfterDiscovery()
    {
        return afterDiscovery;

    }

    public void setAfterDiscovery(String afterDiscovery)
    {
        this.afterDiscovery = afterDiscovery;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }
}
