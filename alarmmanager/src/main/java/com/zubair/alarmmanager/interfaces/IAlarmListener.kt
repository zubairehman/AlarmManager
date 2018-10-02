package com.zubair.alarmmanager.interfaces

import android.content.Context
import android.content.Intent

interface IAlarmListener {
    fun perform(context: Context, intent: Intent)
}
