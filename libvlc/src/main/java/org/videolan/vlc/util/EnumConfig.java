package org.videolan.vlc.util;

/**
 * Created by Mr.CSH on 2017/3/20.
 */

public class EnumConfig {
    //播放样式 展开、缩放
    public class PageType {
        public final static int EXPAND = 0;
        public final static int SHRINK = 1;
    }

    //进度条状态
    public class ProgressState {
        public final static int START = 0;
        public final static int DOING = 1;
        public final static int STOP = 2;
    }

    //播放状态，pause和stop用来判断判断操作播放键的功能
    public class PlayState {
        public final static int PLAY = 1;
        public final static int PAUSE = 1;
        public final static int STOP = 2;
    }
}
