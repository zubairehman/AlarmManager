package com.zubair.alarmmanager.builder

import android.content.Context

import com.zubair.alarmmanager.enums.AlarmType
import com.zubair.alarmmanager.interfaces.AlarmListener

internal class Alarm(var context: Context?, var id: String?, var time: Long, var alarmType: AlarmType?, var alarmListener: AlarmListener?)
