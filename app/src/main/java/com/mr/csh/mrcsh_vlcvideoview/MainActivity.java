package com.mr.csh.mrcsh_vlcvideoview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.videolan.vlc.MyVlcVideoView;
import org.videolan.vlc.util.CoreUtil;
import org.videolan.vlc.util.DensityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.video_layout)
    RelativeLayout mVideoLayout;
    @BindView(R.id.video_view)
    MyVlcVideoView mVideoView;
    @BindView(R.id.v_play)
    ImageView mPlayBtn;
    @BindView(R.id.v_cover)
    ImageView mCover;

    private static final String VIDEO_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private static final String VIDEO_TITLE = "大雄兔";
    //以下两个常量用来保存每个视频上一次播放位置，根据项目需要去修改
    private static final String SP_FILE_NAME = "CshVlcDemo";
    private static final String SP_VIDEO_KEY = "CshVlc_01";   //不同视频key需要设置不同，否则播放seekto读取上一次播放位置可能会出错

    private Activity mActivity;
    private boolean mIsNoWifiPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initNetReceiver();
        initView();
    }

    //注册网络变化监听广播
    private void initNetReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
    }

    //初始化视频播放器控件
    private void initView() {
        mActivity = this;
        mVideoView.setVideoPlayCallback(mVideoPlayCallback);
        mVideoView.setIsVideoBackFinish(false);
        mVideoView.showFullImage();

        mVideoLayout.getLayoutParams().height = Utils.calcHeightByWidthPx(this, DensityUtil.getWidthInPx(this), R.drawable.default_cover);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        GlideOptions.showImage(this, "", mCover, R.drawable.default_cover);
        GlideOptions.showImage(this, "", mPlayBtn, R.drawable.selector_vplay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mVideoView.getCurPlayVideoId().isEmpty()) {
            mVideoView.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mVideoView.getCurPlayVideoId().isEmpty()) {
            mVideoView.onActivityPause();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            int orientation = getRequestedOrientation();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                    || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                mVideoView.onVideoWindowTurn();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.v_play})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_play:
                readyPlayVideo();
                break;
        }
    }

    private void readyPlayVideo() {
        boolean isWifiConnected = CoreUtil.isWifiConnected(this);
        if (/*!isLocalVideo &&*/ !isWifiConnected && !mIsNoWifiPlay) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("wifi未开启，继续播放将产生流量，是否要这样做？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mIsNoWifiPlay = true;
                    mVideoView.setIsNoWifiPlay(true);
                    startVideoPlay();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();
        } else {
            startVideoPlay();
        }
    }

    private void startVideoPlay() {
        mVideoView.setVisibility(View.VISIBLE);
        mVideoView.startPlayVideo(VIDEO_URL, VIDEO_TITLE, SP_FILE_NAME, SP_VIDEO_KEY);
    }

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //视频未播放或者暂停不处理网络变化
            if (mVideoView.getCurPlayVideoId().isEmpty() && mVideoView.getPlayState() != org.videolan.vlc.util.EnumConfig.PlayState.PLAY) {
                return;
            }

            /*//本地视频不处理网络变化
            if (isLocalVideo) {
                return;
            }*/

            boolean isWifiConnected = CoreUtil.isWifiConnected(mActivity);
            boolean isNetAvailable = CoreUtil.isNetworkAvailable(mActivity);

            if (!isWifiConnected && !mIsNoWifiPlay) {
                mVideoView.setVideoPause();

                if (isNetAvailable) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("提示");
                    builder.setMessage("wifi未开启，继续播放将产生流量，是否要这样做？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mIsNoWifiPlay = true;
                            mVideoView.setIsNoWifiPlay(true);
                            mVideoView.goOnPlay();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                }
            }
        }
    };

    private MyVlcVideoView.VideoPlayCallbackImpl mVideoPlayCallback = new MyVlcVideoView.VideoPlayCallbackImpl() {

        @Override
        public void onPlayComplete() {
            mVideoView.setVisibility(View.GONE);
            //如果是水平，则恢复为垂直
            int orientation = getRequestedOrientation();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                    || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mVideoView.setPageTypeByBoolean(true);
            }

            Toast.makeText(mActivity, "播放结束", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVideoNotExist() {

        }

        @Override
        public void onOrientationLandscape(int width, int height) {
            getSupportActionBar().hide();
            mVideoView.showVideoTopLayout(true);

            mVideoLayout.getLayoutParams().width = width;
            mVideoLayout.getLayoutParams().height = height;
        }

        @Override
        public void onOrientationPortrait(int width, int height) {
            getSupportActionBar().show();
            mVideoView.showVideoTopLayout(false);

            mVideoLayout.getLayoutParams().width = width;
            mVideoLayout.getLayoutParams().height = height;
        }

        @Override
        public void onError() {
        }

        @Override
        public void unSupportCPU() {
            mVideoView.setVisibility(View.GONE);
            //如果是水平，则恢复为垂直
            int orientation = getRequestedOrientation();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                    || orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mVideoView.setPageTypeByBoolean(true);
            }
        }

        @Override
        public void onEventPlay(boolean isPlaying) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除广播
        if (myNetReceiver != null) {
            unregisterReceiver(myNetReceiver);
        }
        mVideoView.onActivityDestroy();
    }
}
