package com.wheretogo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.Dimension;

public class DisplayUtils {
    public static int dpToPx(Context context, @Dimension(unit = Dimension.DP) int dp) {
        Resources r  = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (float) dp,
                r.getDisplayMetrics());
    }

    public static int spToPx(Context context, @Dimension(unit = Dimension.SP) int sp) {
        Resources r  = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                (float) sp,
                r.getDisplayMetrics());
    }
}
