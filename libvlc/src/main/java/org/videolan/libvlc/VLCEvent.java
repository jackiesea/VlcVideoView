package org.videolan.libvlc;

abstract class VLCEvent {
    public final int type;
    protected final long arg1;
    protected final long arg2;
    protected final float argf1;

    VLCEvent(int type) {
        this.type = type;
        this.arg1 = this.arg2 = 0;
        this.argf1 = 0.0f;
    }

    VLCEvent(int type, long arg1) {
        this.type = type;
        this.arg1 = arg1;
        this.arg2 = 0;
        this.argf1 = 0.0f;
    }

    VLCEvent(int type, long arg1, long arg2) {
        this.type = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.argf1 = 0.0f;
    }

    VLCEvent(int type, float argf) {
        this.type = type;
        this.arg1 = this.arg2 = 0;
        this.argf1 = argf;
    }

    void release() {
        /* do nothing */
    }

    /**
     * Listener for libvlc events
     *
     * @see VLCEvent
     */
    public interface Listener<T extends VLCEvent> {
        void onEvent(T event);
    }
}
