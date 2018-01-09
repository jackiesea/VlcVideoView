package org.videolan.libvlc.util;

import android.net.Uri;
import android.support.annotation.MainThread;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

public class Dumper {
    public interface Listener {
        void onFinish(boolean success);

        void onProgress(float progress);
    }

    private final LibVLC mLibVLC;
    private final MediaPlayer mMediaPlayer;
    private final Listener mListener;

    /**
     * Create a Dumper that will download an Uri into a local filesystem path
     *
     * @param uri      the Uri to dump
     * @param filepath local filesystem path where to dump the Uri
     * @param listener listener in order to be notified when the dump is finished
     */
    @MainThread
    public Dumper(Uri uri, String filepath, Listener listener) {
        if (uri == null || filepath == null || listener == null)
            throw new IllegalArgumentException("arguments shouldn't be null");
        mListener = listener;

        ArrayList<String> options = new ArrayList<>(8);
        options.add("--demux");
        options.add("dump2,none");
        options.add("--demuxdump-file");
        options.add(filepath);
        options.add("--no-video");
        options.add("--no-audio");
        options.add("--no-spu");
        options.add("-vv");
        mLibVLC = new LibVLC(null, options);

        final Media media = new Media(mLibVLC, uri);
        mMediaPlayer = new MediaPlayer(media);
        mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                switch (event.type) {
                    case MediaPlayer.Event.Buffering:
                        mListener.onProgress(event.getBuffering());
                        break;
                    case MediaPlayer.Event.EncounteredError:
                    case MediaPlayer.Event.EndReached:
                        mListener.onFinish(event.type == MediaPlayer.Event.EndReached);
                        cancel();
                        break;
                }
            }
        });
        media.release();
    }

    /**
     * Start to dump
     */
    @MainThread
    public void start() {
        mMediaPlayer.play();
    }

    /**
     * Cancel the dump of the Uri.
     * Don't call this method if you already received the {@link Listener#onFinish(boolean)} callback.
     */
    @MainThread
    public void cancel() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mLibVLC.release();
    }
}
