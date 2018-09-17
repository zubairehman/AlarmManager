package com.zubair.alarmmanager.builder;

import android.content.Context;

import com.zubair.alarmmanager.enums.AlarmType;
import com.zubair.alarmmanager.interfaces.AlarmListener;

class Alarm {
    private Context context;
    private String id;
    private long time;
    private AlarmType alarmType;
    private AlarmListener alarmListener;

    public Alarm(Context context, String id, long time, AlarmType alarmType, AlarmListener alarmListener) {
        this.context = context;
        this.id = id;
        this.time = time;
        this.alarmType = alarmType;
        this.alarmListener = alarmListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public AlarmType getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmType alarmType) {
        this.alarmType = alarmType;
    }

    public AlarmListener getAlarmListener() {
        return alarmListener;
    }

    public void setAlarmListener(AlarmListener alarmListener) {
        this.alarmListener = alarmListener;
    }
}
