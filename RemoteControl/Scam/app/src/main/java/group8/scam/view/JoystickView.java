package group8.scam.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import group8.scam.controller.handlers.HandleThread;

import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;

/**
 * @author Samuel Bäckström
 * This is the joystick view that is used in the main activity.
 * The class is a subclass of View.
 * The View class represents the basic building block for user interface components.
 * A View occupies a rectangular area on the screen and is responsible for drawing and event handling.
 */

public class JoystickView extends View {

    /* Get's the instance of HandleThread so that it can send messages to the Handler. */
    private HandleThread mHandle = HandleThread.getInstance();

    /* The Paint class holds the style and color information about how to draw geometries, text and bitmaps. */
    private Paint paintBackground = new Paint();
    private Paint paintCircle = new Paint();

    /* Parse the color string (HEX), and return the corresponding color-int. */
    private int colorDarkGrey = Color.parseColor("#AFAFAF");
    private int colorLightGrey = Color.parseColor("#DBDBDB");

    /**
     * The pos variables keep track of the position of both circles.
     * circleRadius is the actual joystick circle, while the backgroundRadius is bigger circle behind the joystick.
     */
    private int posX;
    private int posY;
    private int centerPosX;
    private int centerPosY;
    private int circleRadius;
    private int backgroundRadius;

    /* This string is used to store the data from the joystick (of type string) to the car. */
    private String dataStr;

    private long lastUpdate;

    /* Default constructors inherited from View, the JoystickView(Context, AttributeSet, int) constructor is used. */
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

    /* This method is called in the constructor, sets the the color and anti-aliasing of the Paint objects. */
    private void init(AttributeSet attributeSet, int defStyle) {
        paintBackground.setColor(colorLightGrey);
        paintBackground.setAntiAlias(true);

        paintCircle.setColor(colorDarkGrey);
        paintCircle.setAntiAlias(true);

    }

    /**
     * This method is overridden from View. It is continuously called to redraw the view with new values.
     * The drawCircle() method draws the specified circle using the specified paint.
     * The pos variables define where on the screen the view is drawn.
     * When the pos variables change, the view will be drawn on different parts of the screen.
     * This is what is making it move when you touch it.
     * @param canvas The canvas represent the screen on which the view is drawn on.
     */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerPosX, centerPosY, backgroundRadius, paintBackground);
        canvas.drawCircle(posX, posY, circleRadius, paintCircle);
    }

    /**
     * This is called during layout when the size of this view has changed.
     * If you were just added to the view hierarchy, you're called with the old values of 0.
     */
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        initPosition();
    }

    /**
     * This method initializes all the pos and radius variables.
     * The value is proportional to the screen size of the phone is it used on.
     * getWidth() and getHeight() return the width and height of the screen.
     */
    private void initPosition() {
        posX = getWidth() / 2;
        posY = getHeight() / 2;
        centerPosX = getWidth() / 2;
        centerPosY = getHeight() / 2;
        backgroundRadius = getHeight() / 3;
        circleRadius = getHeight() / 7;
    }

    /**
     * This method override onTouchEvent() from View.
     * It handles touch screen motion events.
     * The method is invoked whenever the screen is touched.
     * @param event Object used to report movement (mouse, pen, finger, trackball) events.
     *              In this case just the X and Y position of where the movement or "touch" took place.
     * @return Returns true if the event was handled, false otherwise.
     */
    public boolean onTouchEvent(MotionEvent event) {

        /* Get the position of the event. */
        posX = (int) event.getX();
        posY = (int) event.getY();

        /* This value ensures that you cannot move the joystick beyond the edges of the background circle */
        double abs = Math.sqrt((posX - centerPosX) * (posX - centerPosX)
                + (posY - centerPosY) * (posY - centerPosY));

        /* If abs is greater than the background radius, the X and Y position stop increasing */
        if (abs > backgroundRadius) {
            posX = (int) ((posX - centerPosX) * backgroundRadius / abs + centerPosX);
            posY = (int) ((posY - centerPosY) * backgroundRadius / abs + centerPosY);
        }

        /**
         * getAction() returns the kind of action being performed.
         * ACTION_UP fires when the screen is no longer touched.
         * ACTION_DOWN fires when the screen is touched initially.
         * ACTION_MOVE fires when the object that is touching the screen is moving.
         * If ACTION_UP fires, the dataStr stores the value " STOP".
         * And the joystick position is reset to the middle of the background circle.
         * If ACTION_DOWN or ACTION_MOVE fires the angle and strength of the joystick are used to
         * calculate the speed and angle that the car will use.
         * The angle and speed variables are used to create a string that is stored in the dataStr.
         */
        if (event.getAction() == MotionEvent.ACTION_UP) {
            for (int i = 0; i < 5; i++) {
                dataStr = " STOP";
                mHandle.sendMessage(MESSAGE_WRITE, dataStr);
            }
            resetCirclePosition();
        } else if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            int angle = getCarAngle(getAngle());
            int speed = getCarSpeed(getAngle(), getStrength());
            dataStr = angle + ":" + speed + ":";
        }

        /**
         * Sends a message that contains the dataStr to the Handler inside the HandleThread.
         * msg.obj is set to the dataStr.
         */
        long currTime = System.currentTimeMillis();
        if ((currTime - lastUpdate) > 25) {
            lastUpdate = currTime;
            System.out.println(dataStr);
            mHandle.sendMessage(MESSAGE_WRITE, dataStr);
        }

        /**
         * If the view is visible, onDraw(android.graphics.Canvas) will be called at some point in the future,
         * through invalidate().
         */
        invalidate();

        return true;
    }

    /* centerPosX and centerPosY represent the middle of the background circle. */
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

    /**
     * This method formats the angle data from the joystick into values the car can use.
     * For example if the data is between certain points, like data > 85 and data <= 95,
     * the return value should be 0, because 0 is the value that makes the car move forward
     * in a straight line.
     * @param data The angle of the joystick.
     */
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

    /**
     * This method either returns the speed as it already is or it negates it and returns it
     * depending on the angle value.
     * @param angle the angle from the joystick
     * @param speed the strength of the joystick, or how far the joystick circle is from the center of the background circle.
     */
    public int getCarSpeed(int angle, int speed) {
        if (angle >= 0 && angle <= 180) {
            return speed;
        } else if (angle > 180 && angle <= 360) {
            return -speed;
        }
        return 1000;
    }
}
