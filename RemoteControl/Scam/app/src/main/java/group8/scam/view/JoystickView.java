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

    private Paint _paintJoystickOne = new Paint();
    private Paint _paintJoystickTwo = new Paint();
    private Paint _paintOutLine = new Paint();

    private int _colorWhite = Color.parseColor("#FFFFFF");
    private int _colorDarkGrey = Color.parseColor("#BFBFBF");
    private int _colorLightGrey = Color.parseColor("#E5E5E5");


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

        _paintJoystickOne.setColor(_colorDarkGrey);
        _paintJoystickOne.setAntiAlias(true);

        _paintJoystickTwo.setColor(_colorLightGrey);
        _paintJoystickTwo.setAntiAlias(true);

        _paintOutLine.setColor(Color.BLACK);
        _paintOutLine.setAntiAlias(true);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (int) (getWidth() / 3.5), _paintJoystickTwo);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 10, _paintJoystickOne);
    }

    public boolean onTouchEvent(MotionEvent event) {


        return false;
    }
}
