package group8.scam.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import group8.scam.model.radar.RadarData;

import static group8.scam.R.color.colorGreen;

/**
 * Created by Firsou on 2017-04-07.
 */

public class RadarView extends View {

    private RadarData radarData;

    private int[] pointsX = new int[15];
    private int[] pointsY = new int[15];
    private int i;

    private double endXc, endYc;
    private double endX, endY;
    private int startX, startY;
    private int x = 752;
    private int y = 1000;
    private int stopY = 70;
    private int pastSonicReading = 0;
    private int pastSonicReading2 = 0;

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

        pastSonicReading = sonicReading;
        pastSonicReading2 = pastSonicReading;

        if (pastSonicReading == 0 && pastSonicReading2 == 0)
            sonicReading = 0;

        System.out.println(sonicReading);

        startX = x;
        startY = y;

        if (angleReading > 90) {
            angleReading = angleReading - 270;
        } else if (angleReading < 90){
            angleReading += 90;
        } else {
            angleReading = 180;
        }

        endX   = x + 1000 * (Math.sin(angleReading * (Math.PI / 180)));
        endY   = y + 1000 * (Math.cos(angleReading * (Math.PI / 180)));

        if (sonicReading == 0)
            sonicReading = 1000;

        canvas.drawLine(startX, startY, (int)endX, (int)endY, paint);

        endXc = x + ((sonicReading * 14)) * (Math.sin(angleReading * (Math.PI / 180)));
        endYc = y + ((sonicReading * 14)) * (Math.cos(angleReading * (Math.PI / 180)));

        pointsX[i] = (int)endXc;
        pointsY[i] = (int)endYc;

        canvas.drawCircle(pointsX[0], pointsY[0], 15, paint);
        canvas.drawCircle(pointsX[1], pointsY[1], 15, paint);
        canvas.drawCircle(pointsX[2], pointsY[2], 15, paint);
        canvas.drawCircle(pointsX[3], pointsY[3], 15, paint);
        canvas.drawCircle(pointsX[4], pointsY[4], 15, paint);
        canvas.drawCircle(pointsX[5], pointsY[5], 15, paint);
        canvas.drawCircle(pointsX[6], pointsY[6], 15, paint);
        canvas.drawCircle(pointsX[7], pointsY[7], 15, paint);
        canvas.drawCircle(pointsX[8], pointsY[8], 15, paint);
        canvas.drawCircle(pointsX[9], pointsY[9], 15, paint);
        canvas.drawCircle(pointsX[10], pointsY[10], 15, paint);
        canvas.drawCircle(pointsX[11], pointsY[11], 15, paint);
        canvas.drawCircle(pointsX[12], pointsY[12], 15, paint);
        canvas.drawCircle(pointsX[13], pointsY[13], 15, paint);
        canvas.drawCircle(pointsX[14], pointsY[14], 15, paint);

        i++;
        if (i == 15)
            i = 0;

        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
    }

}