package fr.univ_tours.polytech.projetlibre.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.Objective;

public class ObjectiveFoundView extends LinearLayout implements View.OnClickListener
{
    private TextView textView = null;

    public ObjectiveFoundView(Context context)
    {
        super(context);
        init(null, 0);
    }

    public ObjectiveFoundView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, 0);
    }

    public ObjectiveFoundView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle)
    {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.objective_found_view, this, true);


        textView = (TextView) findViewById(R.id.objectiveFoundTextView);

        Button closeButton = (Button) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);
    }

    public void setObjective(Objective objective)
    {
        textView.setText("Bravo, vous avez trouve un objectif !\n" + objective.textAfterDiscovery);
    }

    @Override
    public void onClick(View v)
    {
        this.setVisibility(View.INVISIBLE);
    }
}
