package com.xmpppush.client;

import android.util.Log;

public class ReconnectionThread extends Thread {

    private static final String LOGTAG = CommonUtil
            .makeLogTag(ReconnectionThread.class);

    private final XmppManager xmppManager;

    private int waiting;

    ReconnectionThread(XmppManager xmppManager) {
        this.xmppManager = xmppManager;
        this.waiting = 0;
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                Log.d(LOGTAG, "Trying to reconnect in " + waiting()
                        + " seconds");
                Thread.sleep((long) waiting() * 1000L);
                xmppManager.connect();
                waiting++;
            }
        } catch (final InterruptedException e) {
            xmppManager.getHandler().post(new Runnable() {
                public void run() {
                    xmppManager.getConnectionListener().reconnectionFailed(e);
                }
            });
        }
    }

    private int waiting() {
        if (waiting > 20) {
            return 600;
        }
        if (waiting > 13) {
            return 300;
        }
        return waiting <= 7 ? 10 : 60;
    }
}
