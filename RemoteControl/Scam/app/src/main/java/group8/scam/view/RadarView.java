package group8.scam.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

    private double endX, endY;
    private int startX, startY;
    private int x = 878;
    private int y = 1280;
    private int stopY = 70;

    private Paint paint = new Paint();

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
        }

        if (radarData != null) {
            sonicReading = radarData.getUltrasonicReading();
        }


        startX = x;
        startY = y;


        if (angleReading > 90) {
            angleReading = angleReading - 270;
        } else if (angleReading < 90){
            angleReading += 90;
        } else {
            angleReading = 180;
        }


        endX   = x + 1210 * (Math.sin(angleReading * (Math.PI / 180)));
        endY   = y + 1210 * (Math.cos(angleReading * (Math.PI / 180)));


        canvas.drawLine(startX, startY, (int)endX, (int)endY, paint);
        canvas.drawCircle((int)endX, (int)endY, 15, paint);

        if (sonicReading > 0) {
            drawPoint(canvas, sonicReading, (int) endX);
        }

        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
    }

    private void drawPoint (Canvas canvas, int sonicReading, int endX) {

        Paint newPaint = new Paint();
        newPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorGreen));

        canvas.drawCircle(endX, sonicReading, 10, newPaint);
    }

}