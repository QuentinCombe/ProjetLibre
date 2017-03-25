package fr.univ_tours.polytech.projetlibre.controller;


import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.controller.MainActivity;
import fr.univ_tours.polytech.projetlibre.model.ObjectiveFoundDataModel;

public class ObjectiveListViewAdapter extends ArrayAdapter<ObjectiveFoundDataModel>
{
    private ArrayList<ObjectiveFoundDataModel> dataSet;
    private LayoutInflater inflater = null;
    Context mContext;

    public ObjectiveListViewAdapter(ArrayList<ObjectiveFoundDataModel> data, Context context)
    {
        super(context, R.layout.objective_found_row, data);
        this.dataSet = data;
        this.mContext=context;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ObjectiveFoundDataModel dataModel = getItem(position);

        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.objective_found_row, null);

        TextView id = (TextView) vi.findViewById(R.id.numObjectiveTextView);
        TextView afterDiscovery = (TextView) vi.findViewById(R.id.afterDiscoveryTextView);
        TextView location = (TextView) vi.findViewById(R.id.locationTextView);

        id.setText(dataModel.getId());
        afterDiscovery.setText(dataModel.getAfterDiscovery());
        location.setText(dataModel.getLocation());

        return vi;
    }

}