package group8.scam.model.dpad;

import android.os.Message;
import android.view.MotionEvent;

import group8.scam.controller.handlers.HandleThread;

import static group8.scam.model.communication.DataThread.MESSAGE_WRITE;

/**
 * Created by marku on 2017-04-28.
 */

public class DpadLogic {
    private String dataStr;

    private HandleThread handler = HandleThread.getInstance();

        /*
        *logic for the up button in dpad
        *
         */
    public boolean up (MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            dataStr = "0:50:";
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            dataStr = " STOP";
        }
        Message msg = handler.getHandler().obtainMessage();
        msg.what = MESSAGE_WRITE;
        msg.obj = dataStr;
        msg.sendToTarget();

        return true;
    }

        /*
        *logic for the dow button in dpad
        *
         */
    public boolean down (MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            dataStr = "0:-50:";
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            dataStr = " STOP";
        }
        Message msg = handler.getHandler().obtainMessage();
        msg.what = MESSAGE_WRITE;
        msg.obj = dataStr;
        msg.sendToTarget();

        return true;
    }


        /*
        *logic for the right button in dpad
        *
         */
public boolean right (MotionEvent event){
    if(event.getAction() == MotionEvent.ACTION_DOWN){
        dataStr = "50:50:";
    }
    if(event.getAction() == MotionEvent.ACTION_UP){
        dataStr = " STOP";
    }
    Message msg = handler.getHandler().obtainMessage();
    msg.what = MESSAGE_WRITE;
    msg.obj = dataStr;
    msg.sendToTarget();

    return true;
    }

        /*
        *logic for the left button in dpad
        *
         */
    public boolean left (MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            dataStr = "-50:50:";
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            dataStr = " STOP";
        }
        Message msg = handler.getHandler().obtainMessage();
        msg.what = MESSAGE_WRITE;
        msg.obj = dataStr;
        msg.sendToTarget();


        return true;
    }


}
