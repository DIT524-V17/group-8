package group8.scam.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import group8.scam.R;
import group8.scam.model.radar.RadarData;

/**
 * Created by Firsou on 2017-04-07.
 */

public class RadarView extends View {

    private Bitmap radar = BitmapFactory.decodeResource(getResources(), R.drawable.radarfordemo);
    private Canvas canvas = new Canvas();

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initRadar() {
        RadarData radarData = new RadarData(0, 0);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }



}
