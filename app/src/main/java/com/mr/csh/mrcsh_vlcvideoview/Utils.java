package com.mr.csh.mrcsh_vlcvideoview;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Mr.CSH on 2017/11/28.
 */

public class Utils {
    public static int calcHeightByWidthPx(Context context, float width, int defaultDrawable) {
        Drawable bannerDefault = context.getResources().getDrawable(defaultDrawable);
        int w = bannerDefault.getIntrinsicWidth();
        int h = bannerDefault.getIntrinsicHeight();
        return (int) (h * width / w);
    }
}
