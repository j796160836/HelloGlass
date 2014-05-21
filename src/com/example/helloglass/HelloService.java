package com.example.helloglass;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

/**
 * Service owning the LiveCard living in the timeline.
 */
public class HelloService extends Service {

    private static final String LIVE_CARD_TAG = "hellocard";

    /**
     * Binder giving access to the underlying {@code Timer}.
     */
    public static class TimerBinder extends Binder {
//        private Timer mTimer;
//
//        public TimerBinder(Timer timer) {
//            mTimer = timer;
//        }
//
//        public Timer getTimer() {
//            return mTimer;
//        }
    }

    private HelloDrawer mTimerDrawer;

    private LiveCard mLiveCard;

    @Override
    public void onCreate() {
        super.onCreate();
        mTimerDrawer = new HelloDrawer(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
//        return new TimerBinder(mTimerDrawer.getTimer());
    	 return new TimerBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            mLiveCard = new LiveCard(this, LIVE_CARD_TAG);

            mLiveCard.setDirectRenderingEnabled(true).getSurfaceHolder().addCallback(mTimerDrawer);

            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
            mLiveCard.attach(this);
            mLiveCard.publish(PublishMode.REVEAL);
        } else {
            mLiveCard.navigate();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        super.onDestroy();
    }
}
