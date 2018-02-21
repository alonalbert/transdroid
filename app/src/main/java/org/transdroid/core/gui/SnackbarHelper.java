package org.transdroid.core.gui;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * A wrapper around Snackbar to make things easier.
 */
public class SnackbarHelper {
    public static void show(Activity activity, String text, int color) {
        final Snackbar snackbar = Snackbar.make(
                getRootView(activity),
                text,
                Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, color));
        snackbar.show();
    }

    public static void show(Activity activity, int textResId, int color) {
        final Snackbar snackbar = Snackbar.make(
                getRootView(activity),
                textResId,
                Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity, color));
        snackbar.show();
    }

    public static void show(Activity activity, String text) {
        Snackbar.make(getRootView(activity), text, Snackbar.LENGTH_SHORT)
                .show();
    }

    public static void show(Activity activity, int textResId) {
        Snackbar.make(getRootView(activity), textResId, Snackbar.LENGTH_SHORT)
                .show();
    }

    private static View getRootView(Activity activity) {
        return activity.findViewById(android.R.id.content);
    }
}
