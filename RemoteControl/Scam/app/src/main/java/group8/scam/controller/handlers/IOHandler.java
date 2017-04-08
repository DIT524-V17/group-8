package group8.scam.controller.handlers;

import android.os.Handler;

/**
 * Created by sambac on 2017-04-07.
 */

public class IOHandler {
    private static final Handler mHandler = new Handler();

    private IOHandler() {

    }

    public static Handler getInstance() {
        return mHandler;
    }
}
