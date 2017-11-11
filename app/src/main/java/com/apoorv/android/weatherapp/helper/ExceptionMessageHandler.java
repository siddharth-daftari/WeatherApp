package com.apoorv.android.weatherapp.helper;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by siddharthdaftari on 11/11/17.
 */

public class ExceptionMessageHandler {

    public static Context context = null;

    public static void handleError(Context c, String message, Exception e, HashMap<String, Object> extraParams){
        System.out.println("-----> EXCEPTION: " + message + "  -----> context: " + context);
        e.printStackTrace();

//        AlertDialog.Builder messageBox = new AlertDialog.Builder(context);
//        messageBox.create().getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        messageBox.setTitle("Alert");
//        messageBox.setMessage(message);
//        messageBox.setCancelable(false);
//        messageBox.setNeutralButton("OK", null);
//        messageBox.show();
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();

    }
}
