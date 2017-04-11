package group8.scam.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.nio.ByteBuffer;

import group8.scam.R;
import group8.scam.controller.handlers.HandleThread;

import static group8.scam.model.communication.DataThread.KEY;
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

        if (event.getAction() == MotionEvent.ACTION_UP) {
            byte[] bytes = ByteBuffer.allocate(4).putInt(1).array();

            Bundle bundle = new Bundle();
            bundle.putByteArray(KEY, bytes);

            Message msg = mHandle.getHandler().obtainMessage();
            msg.setData(bundle);
            msg.what = MESSAGE_WRITE;
            msg.sendToTarget();
            resetCirclePosition();
        }

        double abs = Math.sqrt((posX - centerPosX) * (posX - centerPosX)
                + (posY - centerPosY) * (posY - centerPosY));


        if (abs > backgroundRadius) {
            posX = (int) ((posX - centerPosX) * backgroundRadius / abs + centerPosX);
            posY = (int) ((posY - centerPosY) * backgroundRadius / abs + centerPosY);
        }

        byte[] angleByteArray = ByteBuffer.allocate(4).putInt(getAngle()).array();
        byte[] strengthByteArray = ByteBuffer.allocate(4).putInt(getStrength()).array();

        Bundle angleData = new Bundle();
        angleData.putByteArray(KEY, angleByteArray);

        Message angleMsg = mHandle.getHandler().obtainMessage();
        angleMsg.setData(angleData);
        angleMsg.what = MESSAGE_WRITE;
        angleMsg.sendToTarget();


        Bundle strengthData = new Bundle();
        strengthData.putByteArray(KEY, strengthByteArray);

        Message strengthMsg = mHandle.getHandler().obtainMessage();
        strengthMsg.setData(strengthData);
        strengthMsg.what = MESSAGE_WRITE;
        strengthMsg.sendToTarget();


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
}
