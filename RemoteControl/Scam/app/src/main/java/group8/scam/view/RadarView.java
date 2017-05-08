package group8.scam.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import group8.scam.R;
import group8.scam.model.radar.RadarData;

/**
 * Created by Firsou on 2017-04-07.
 */

public class RadarView extends View {

    private RadarData radarData;
    private Bitmap radar = BitmapFactory.decodeResource(getResources(), R.drawable.radarfordemo);
    //private Canvas radarCanvas = new Canvas(radar);
    private Paint paint = new Paint();

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        radarData = new RadarData(0, 0);
        System.out.println("RadarData created.");
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radarData != null)
            System.out.println("Angle of Servo: " + radarData.getAngleOfServo());

        if (radarData != null)
            System.out.println("Ultrasonic Reading: " + radarData.getUltrasonicReading());

    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
    }

}