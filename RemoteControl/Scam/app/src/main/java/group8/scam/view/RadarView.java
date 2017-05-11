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

import static group8.scam.R.color.colorGreen;

/**
 * Created by Firsou on 2017-04-07.
 */

public class RadarView extends View {

    private RadarData radarData;
    private Bitmap radar = BitmapFactory.decodeResource(getResources(), R.drawable.radarfordemo);
    private Canvas radarCanvas = new Canvas(radar.copy(Bitmap.Config.ARGB_8888, true));


    private Paint paint = new Paint();
    private Path line = new Path();

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        radarData = new RadarData(0, 0);
        paint.setColor(getResources().getColor(colorGreen));
        paint.setStrokeWidth(10);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int angleReading = 0;
        int sonicReading = 0;

        if (radarData != null) {
            angleReading = radarData.getAngleOfServo();
            System.out.println("AngleReading: " + angleReading);
        }

        if (radarData != null) {
            sonicReading = radarData.getUltrasonicReading();
            System.out.println("SonicReading: " + sonicReading);
        }

        System.out.println("In onDraw()");
        canvas.drawLine(878, 1280, 750 - (angleReading * 3), 100, paint);

        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
    }
}