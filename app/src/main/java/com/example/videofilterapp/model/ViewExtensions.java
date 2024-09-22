package com.example.videofilterapp.model;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Change width x height
 */
public class ViewExtensions {

    public static void resizeView(View view, double w, double h) {

        Activity activity = (Activity) view.getContext();
        int maxWidth = screenWidth(activity);
        int maxHeight = screenHeight(activity) - (2 * toPx(56));

        double scale = Math.min(
                maxWidth / w,
                maxHeight / h);

        view.getLayoutParams().width = (int) (w * scale);
        view.getLayoutParams().height = (int) (h * scale);
        view.requestLayout();
    }

    /**
     * Convert dp to px
     */
    public static int toPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Get screen width in pixels
     */
    public static int screenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * Get screen height in pixels
     */
    public static int screenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private static final float MAX_HUE = 0.1f;
    private static final int MAX_GRAIN = 360;
    private static final float MAX_PROGRESS = 100f;

    // Transform input from progress bar to effect intensity
    public static float transformGrain(int progress) {
        return (MAX_HUE * progress) / MAX_PROGRESS;
    }

    public static float transformHue(int progress) {
        return (MAX_GRAIN * progress) / MAX_PROGRESS;
    }

    public static float transformAutofix(int progress) {
        return progress / MAX_PROGRESS;
    }
}

