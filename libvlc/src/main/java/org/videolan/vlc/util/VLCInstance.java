package org.videolan.vlc.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.util.VLCUtil;

public class VLCInstance {
    public final static String TAG = "VLC/UiTools/VLCInstance";

    private static LibVLC sLibVLC = null;

//    private static Runnable sCopyLua = new Runnable() {
//        @Override
//        public void run() {
//            final String destinationFolder = VLCApplication.getAppContext().getDir("vlc",
//                    Context.MODE_PRIVATE).getAbsolutePath() + "/.share/lua";
//            AssetManager am = VLCApplication.getAppResources().getAssets();
//            FileUtils.copyAssetFolder(am, "lua", destinationFolder);
//        }
//    };

    /**
     * A set of utility functions for the VLC application
     */
    public synchronized static LibVLC get(Context getApplicationContext) throws IllegalStateException {
        if (sLibVLC == null) {
            //  Thread.setDefaultUncaughtExceptionHandler(new VLCCrashHandler());

            //    final Context context = VLCApplication.getAppContext();
            if (!VLCUtil.hasCompatibleCPU(getApplicationContext)) {
                Log.e(TAG, VLCUtil.getErrorMsg());
                //  throw new IllegalStateException("LibVLC initialisation failed: " + VLCUtil.getErrorMsg());
            }

            sLibVLC = new LibVLC(getApplicationContext, VLCOptions.getLibOptions(getApplicationContext));
            //    VLCApplication.runBackground(sCopyLua);
        }
        return sLibVLC;
    }

    public static synchronized void restart(Context getApplicationContext) throws IllegalStateException {
        if (sLibVLC != null) {
            sLibVLC.release();
            sLibVLC = new LibVLC(getApplicationContext, VLCOptions.getLibOptions(getApplicationContext));
        }
    }

    public static synchronized boolean testCompatibleCPU(Context context) {
        if (sLibVLC == null && !VLCUtil.hasCompatibleCPU(context)) {
            if (context instanceof Activity) {
//                final Intent i = new Intent(context, CompatErrorActivity.class);
//                context.startActivity(i);
            }
            return false;
        } else
            return true;
    }
}
