package org.videolan.vlc.util;

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

    public class PlayState {
        public static final int STATE_PLAY = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_LOAD = 3;
        public static final int STATE_RESUME = 4;
        public static final int STATE_STOP = 5;
    }
}
