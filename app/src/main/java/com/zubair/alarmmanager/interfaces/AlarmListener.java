package com.zubair.alarmmanager.interfaces;

import android.content.Context;
import android.content.Intent;

public interface AlarmListener {
    void perform(Context context, Intent intent);
}
