package com.zubair.alarmmanager.builder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.zubair.alarmmanager.enums.AlarmType;
import com.zubair.alarmmanager.interfaces.AlarmListener;

import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

public class AlarmBuilder {

    //static variables
    private static final int REQUEST_CODE = 111131;

    //alarm variables
    private AlarmManager alarmManager;
    private BroadcastReceiver broadcastReceiver;
    private PendingIntent pendingIntent;
    private Set<AlarmListener> alarmListenerSet;

    //builder variables
    private Context context;
    private String id;
    private long timeInMilliSeconds = 0;
    private AlarmType alarmType = AlarmType.ONE_TIME;
    private AlarmListener alarmListener;

    public AlarmBuilder with(Context context) {
        this.context = context;
        return this;
    }

    public AlarmBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public AlarmBuilder setTimeInMilliSeconds(long timeInMilliSeconds) {
        this.timeInMilliSeconds = timeInMilliSeconds;
        return this;
    }

    public AlarmBuilder setAlarmType(AlarmType alarmType) {
        this.alarmType = alarmType;
        return this;
    }

    public AlarmBuilder setAlarmListener(AlarmListener alarmListener) {
        this.alarmListener = alarmListener;
        return this;
    }

    public AlarmBuilder build() {
        Alarm alarm = new Alarm(context, id, timeInMilliSeconds, alarmType, alarmListener);

        if (alarm.getContext() == null) {
            throw new IllegalStateException("Context can't be null!");
        }

        if (alarm.getId() == null) {
            throw new IllegalStateException("Id can't be null!");
        }

        if (alarm.getAlarmListener() == null) {
            throw new IllegalStateException("Listener can't be null!");
        }

        //setting alarm
        setAlarm();

        return this;
    }

    private void setAlarm() {

        //initialization
        this.alarmListenerSet = new HashSet<>();
        this.alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);

        //Check if alarm is already running
        Intent intent = new Intent(id);
        boolean alarmRunning = (PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null);

            this.context.registerReceiver(
                    this.broadcastReceiver = this.getBroadcastReceiver(),
                    new IntentFilter(id));

        if (!alarmRunning) {

            //setting alarm
            long ensurePositiveTime = Math.max(this.timeInMilliSeconds, 0L);
            this.pendingIntent = PendingIntent.getBroadcast(this.context, REQUEST_CODE, intent, 0);

            switch (this.alarmType) {
                case REPEAT:
                    this.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ensurePositiveTime, this.pendingIntent);
                    break;
                case ONE_TIME:
                default:
                    this.alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ensurePositiveTime, this.pendingIntent);
            }
        } else {
            Timber.e("Alarm already running.!");
        }

        //setting alarm listener
        addListener(this.alarmListener);
    }

    private BroadcastReceiver getBroadcastReceiver() {

        return new BroadcastReceiver() {

            @Override
            public void onReceive(final Context context, final Intent intent) {

                for (AlarmListener alarmListener : AlarmBuilder.this.alarmListenerSet) {
                    alarmListener.perform(context, intent);
                }
            }
        };
    }

    public void cancelAlarm() {
        this.alarmListenerSet.clear();
        this.alarmManager.cancel(this.pendingIntent);
        this.context.unregisterReceiver(this.broadcastReceiver);
        Timber.e("Alarm has been canceled..!");
    }

    private synchronized void addListener(final AlarmListener alarmListener) {
        this.alarmListenerSet.add(alarmListener);
    }

    public synchronized void removeListener(final AlarmListener alarmListener) {
        this.alarmListenerSet.remove(alarmListener);
    }
}