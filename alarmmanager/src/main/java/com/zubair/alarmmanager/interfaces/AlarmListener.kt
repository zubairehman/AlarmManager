package com.zubair.alarmmanager.interfaces

import android.content.Context
import android.content.Intent

interface AlarmListener {
    fun perform(context: Context, intent: Intent)
}
