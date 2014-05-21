package com.example.helloglass;

import java.util.Timer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Activity showing the options menu.
 */
public class MenuActivity extends Activity {

    /** Request code for setting the timer, visible for testing. */

    private final Handler mHandler = new Handler();

    private boolean mAttachedToWindow;
    private boolean mOptionsMenuOpen;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof HelloService.TimerBinder) {
//                mTimer = ((TimerService.TimerBinder) service).getTimer();
                openOptionsMenu();
            }
            // No need to keep the service bound.
            unbindService(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Nothing to do here.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(this, HelloService.class), mConnection, 0);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
        openOptionsMenu();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttachedToWindow = false;
    }

    @Override
    public void openOptionsMenu() {
        if (!mOptionsMenuOpen && mAttachedToWindow) {
            mOptionsMenuOpen = true;
            super.openOptionsMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setOptionsMenuGroupState(menu, R.id.hello, true);
        setOptionsMenuGroupState(menu, R.id.stop, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.hello:
            	Toast.makeText(this, "I'm here!", Toast.LENGTH_LONG).show();
                return true;
//            case R.id.go_activity:
//                // Start the new Activity at the end of the message queue for proper options menu
//                // animation. This is only needed when starting a new Activity or stopping a Service
//                // that published a LiveCard.
//                post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Intent setTimerIntent =
//                                new Intent(MenuActivity.this, SetTimerActivity.class);
//
//                        setTimerIntent.putExtra(
//                                SetTimerActivity.EXTRA_DURATION_MILLIS, mTimer.getDurationMillis());
//                        startActivityForResult(setTimerIntent, SET_TIMER);
//                    }
//                });
//                return true;
            case R.id.stop:
                // Stop the service at the end of the message queue for proper options menu
                // animation. This is only needed when starting a new Activity or stopping a Service
                // that published a LiveCard.
                post(new Runnable() {

                    @Override
                    public void run() {
                        stopService(new Intent(MenuActivity.this, HelloService.class));
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        mOptionsMenuOpen = false;
            // Nothing else to do, closing the Activity.
//            finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 100) {
//            mTimer.setDurationMillis(data.getLongExtra(SetTimerActivity.EXTRA_DURATION_MILLIS, 0));
        }
        finish();
    }

    /**
     * Posts a {@link Runnable} at the end of the message loop, overridable for testing.
     */
    protected void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    /**
     * Sets all menu items visible and enabled state that are in the given group.
     */
    private static void setOptionsMenuGroupState(Menu menu, int groupId, boolean enabled) {
        menu.setGroupVisible(groupId, enabled);
        menu.setGroupEnabled(groupId, enabled);
    }
}
