package group8.scam.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sambac on 2017-04-03.
 */

public class JoystickView extends View {

    private Paint paintBackground = new Paint();
    private Paint paintOutLine = new Paint();
    private Paint paintCircle = new Paint();

    private int colorWhite = Color.parseColor("#FFFFFF");
    private int colorDarkGrey = Color.parseColor("#BFBFBF");
    private int colorLightGrey = Color.parseColor("#E5E5E5");

    private int posX;
    private int posY;
    private int centerPosX;
    private int centerPosY;
    private int circleRadius;
    private int backgroundRadius;

    public JoystickView(Context context) {
        super(context);
        init(null, 0);
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attributeSet, int defStyle) {
        paintBackground.setColor(colorLightGrey);
        paintBackground.setAntiAlias(true);

        paintCircle.setColor(colorDarkGrey);
        paintCircle.setAntiAlias(true);

        paintOutLine.setColor(colorWhite);
        paintOutLine.setAntiAlias(true);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerPosX, centerPosY, backgroundRadius, paintBackground);
        canvas.drawCircle(posX, posY, circleRadius, paintCircle);
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        initPosition();
    }

    private void initPosition() {
        posX = getWidth() / 2;
        posY = getHeight() / 2;
        centerPosX = getWidth() / 2;
        centerPosY = getHeight() / 2;
        backgroundRadius = (int) (getHeight() / 3);
        circleRadius = getHeight() / 7;
    }

    public boolean onTouchEvent(MotionEvent event) {
        posX = (int) event.getX();
        posY = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            resetCirclePosition();
        }

        double abs = Math.sqrt((posX - centerPosX) * (posX - centerPosX)
                + (posY - centerPosY) * (posY - centerPosY));


        if (abs > backgroundRadius) {
            posX = (int) ((posX - centerPosX) * backgroundRadius / abs + centerPosX);
            posY = (int) ((posY - centerPosY) * backgroundRadius / abs + centerPosY);
        }

        invalidate();

        System.out.println(getAngle());
        System.out.println(getStrength());

        return true;
    }

    private void resetCirclePosition() {
        posX = centerPosX;
        posY = centerPosY;
    }

    /**
     * Process the angle following the 360° counter-clock protractor rules.
     * @return the angle of the button
     */
    private int getAngle() {
        int angle = (int) Math.toDegrees(Math.atan2(centerPosY - posY, posX - centerPosX));
        return angle < 0 ? angle + 360 : angle; // make it as a regular counter-clock protractor
    }


    /**
     * Process the strength as a percentage of the distance between the center and the border.
     * @return the strength of the button
     */
    private int getStrength() {
        return (int) (100 * Math.sqrt((posX - centerPosX)
                * (posX - centerPosX) + (posY- centerPosY)
                * (posY - centerPosY)) / backgroundRadius);
    }
}
