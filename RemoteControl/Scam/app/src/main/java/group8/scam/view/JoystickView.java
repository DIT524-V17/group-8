package group8.scam.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import group8.scam.controller.handlers.HandleThread;
import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;

/**
 * Created by sambac on 2017-04-03.
 */

public class JoystickView extends View {

    private HandleThread mHandle = HandleThread.getInstance();

    private Paint paintBackground = new Paint();
    private Paint paintCircle = new Paint();

    private int colorDarkGrey = Color.parseColor("#AFAFAF");
    private int colorLightGrey = Color.parseColor("#DBDBDB");

    private int posX;
    private int posY;
    private int centerPosX;
    private int centerPosY;
    private int circleRadius;
    private int backgroundRadius;

    private String dataStr;

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
        backgroundRadius = getHeight() / 3;
        circleRadius = getHeight() / 7;
    }

    public boolean onTouchEvent(MotionEvent event) {

        posX = (int) event.getX();
        posY = (int) event.getY();

        double abs = Math.sqrt((posX - centerPosX) * (posX - centerPosX)
                + (posY - centerPosY) * (posY - centerPosY));

        if (abs > backgroundRadius) {
            posX = (int) ((posX - centerPosX) * backgroundRadius / abs + centerPosX);
            posY = (int) ((posY - centerPosY) * backgroundRadius / abs + centerPosY);
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            dataStr = "STOP";
            resetCirclePosition();
        } else if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            int angle = getCarAngle(getAngle());
            int speed = getCarSpeed(getAngle(), getStrength());
            dataStr = angle + " " + speed;
        }

        Message msg = mHandle.getHandler().obtainMessage();
        msg.what = MESSAGE_WRITE;
        msg.obj = dataStr;
        msg.sendToTarget();

        invalidate();

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

    public int getCarAngle(int data) {
        if (data > 85 && data <= 95) {
            return 0;
        } else if (data >= 0 && data <= 85) {
            return 90 - data;
        } else if (data > 95 && data <= 180) {
            return -(data - 90);
        } else if (data > 180 && data <= 265) {
            return data - 270;
        } else if (data > 265 && data <= 275) {
            return 0;
        } else if (data > 275 && data <= 360) {
            return data - 270;
        }
        return 1000;
    }

    public int getCarSpeed(int angle, int speed) {
        if (angle >= 0 && angle <= 180) {
            return speed;
        } else if (angle > 180 && angle <= 360) {
            return -speed;
        }
        return 1000;
    }
}
