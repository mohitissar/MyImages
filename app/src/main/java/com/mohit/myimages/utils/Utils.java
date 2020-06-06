package com.mohit.myimages.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    private static volatile Utils mInstance;
    private Context context;

    private Utils(Context context) {
        this.context = context;
    }

    public static Utils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (Utils.class) {
                if (mInstance == null) {
                    mInstance = new Utils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * Converting dp to pixel
     */
    public int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
