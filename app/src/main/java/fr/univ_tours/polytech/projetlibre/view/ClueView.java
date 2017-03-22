package fr.univ_tours.polytech.projetlibre.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.univ_tours.polytech.projetlibre.R;
import fr.univ_tours.polytech.projetlibre.model.Clue;
import fr.univ_tours.polytech.projetlibre.model.Objective;

public class ClueView extends LinearLayout implements View.OnClickListener
{
    private TextView mClueTextView = null;
    private ImageView mClueImageView = null;

    private View mCantClickView;

    public ClueView(Context context)
    {
        super(context);
        init(null, 0);
    }

    public ClueView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, 0);
    }

    public ClueView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setCantClickView(View cantClickView)
    {
        mCantClickView = cantClickView;
    }

    private void init(AttributeSet attrs, int defStyle)
    {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.clue_view, this, true);


        mClueTextView = (TextView) findViewById(R.id.clueTextView);

        mClueImageView = (ImageView) findViewById(R.id.clueImageView);
    }

    public void setClue(Clue clue)
    {
        mClueImageView.setImageBitmap(clue.image);

        mClueTextView.setText(clue.description);
    }

    @Override
    public void onClick(View v)
    {
        mCantClickView.setVisibility(INVISIBLE);
        this.setVisibility(View.INVISIBLE);

    }
}
