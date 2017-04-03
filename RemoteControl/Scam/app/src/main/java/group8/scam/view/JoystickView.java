package group8.scam.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sambac on 2017-04-03.
 */

public class JoystickView extends View {

    private Paint _paintJoystick = new Paint();

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
        _paintJoystick.setColor(Color.RED);
        _paintJoystick.setAntiAlias(true);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 300, _paintJoystick);
    }

}
