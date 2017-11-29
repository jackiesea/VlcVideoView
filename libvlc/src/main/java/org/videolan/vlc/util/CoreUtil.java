package org.videolan.vlc.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

import io.reactivex.disposables.Disposable;

/**
 * Created by Mr.CSH on 2017/11/28.
 */

public class CoreUtil {
    //解注册观察者模式网络请求
    public static void disposeSubscribe(Disposable... disposables) {
        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    //播放进度
    public static int getPlayProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > 100) {
            progress = 100;
        }
        return progress;
    }

    public static String getPlayTime(long playSecond, long allSecond) {
        String playSecondStr = "00:00";
        String allSecondStr = "--:--";
        if (playSecond > 0) {
            playSecondStr = formatPlayTime(playSecond);
        }
        if (allSecond > 0) {
            allSecondStr = formatPlayTime(allSecond);
        }
        return playSecondStr + "/" + allSecondStr;
    }

    public static String formatPlayTime(long millis) {
        int totalSeconds = (int) millis / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return String.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    //获取垂直时视频高度（宽度满），根据后端给的图片大小和手机像素进行计算
    public static int getVideoHeightPx(Context context) {
        return (int) (369 * DensityUtil.getWidthInPx(context) / 656);
    }

    //判断屏幕是否常亮
    public static boolean isKeepScreenOn(Context context) {
        return (((Activity) context).getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) == WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
    }

    //设置屏幕保持常亮/非常亮
    public static void keepScreenOnOff(Context context, boolean isKeepOn) {
        if (isKeepOn) {
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            }
        }
        return false;
    }

    //当前wifi是否可用
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }
}
