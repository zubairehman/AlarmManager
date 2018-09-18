package com.zubair.alarmmanager.builder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.zubair.alarmmanager.enums.AlarmType
import com.zubair.alarmmanager.interfaces.AlarmListener
import java.util.*

class AlarmBuilder {

    //alarm variables
    private var alarmManager: AlarmManager? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    private var pendingIntent: PendingIntent? = null
    private var alarmListenerSet: MutableSet<AlarmListener>? = null

    //builder variables
    private var context: Context? = null
    private var id: String? = null
    private var timeInMilliSeconds: Long = 0
    private var alarmType = AlarmType.ONE_TIME
    private var alarmListener: AlarmListener? = null

    companion object {
        private const val REQUEST_CODE = 111131
    }

    fun with(context: Context): AlarmBuilder {
        this.context = context
        this.alarmListenerSet = HashSet()
        this.alarmManager = this.context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return this
    }

    fun setId(id: String): AlarmBuilder {
        this.id = id
        return this
    }

    fun setTimeInMilliSeconds(timeInMilliSeconds: Long): AlarmBuilder {
        this.timeInMilliSeconds = timeInMilliSeconds
        return this
    }

    fun setAlarmType(alarmType: AlarmType): AlarmBuilder {
        this.alarmType = alarmType
        return this
    }

    fun setAlarm(): AlarmBuilder {

        val alarm = Alarm(context, id, timeInMilliSeconds, alarmType, alarmListener)

        if (alarm.context == null) {
            throw IllegalStateException("Context can't be null!")
        }

        if (alarm.id == null) {
            throw IllegalStateException("Id can't be null!")
        }

        initAlarm()

        return this
    }

    //General Methods:------------------------------------------------------------------------------
    private fun initAlarm() {

        //creating intent
        val intent = Intent(this.id)
        val alarmRunning = PendingIntent.getBroadcast(this.context, REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null

        //setting broadcast
        this.broadcastReceiver = this.getBroadcastReceiver()
        this.context!!.registerReceiver(
                this.broadcastReceiver,
                IntentFilter(this.id))

        //setting alarm
        val ensurePositiveTime = Math.max(this.timeInMilliSeconds, 0L)
        this.pendingIntent = PendingIntent.getBroadcast(this.context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Check if alarm is already running
        if (!alarmRunning) {
            when (this.alarmType) {
                AlarmType.REPEAT -> this.alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ensurePositiveTime, this.pendingIntent)
                AlarmType.ONE_TIME -> this.alarmManager!!.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ensurePositiveTime, this.pendingIntent)
            }
        } else {
            Log.e("Alarm", "Alarm already running.!")
            updateAlarm()
        }
    }

    fun cancelAlarm() {
        this.alarmListenerSet!!.clear()
        this.alarmManager!!.cancel(this.pendingIntent)
        this.context!!.unregisterReceiver(this.broadcastReceiver)
        Log.e("Alarm", "Alarm has been canceled..!")
    }

    private fun updateAlarm() {

        //calculating alarm time and creating pending intent
        val intent = Intent(this.id)
        val ensurePositiveTime = Math.max(this.timeInMilliSeconds, 0L)
        this.pendingIntent = PendingIntent.getBroadcast(this.context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //removing previously running alarm
        this.alarmManager!!.cancel(this.pendingIntent)
        this.context!!.unregisterReceiver(this.broadcastReceiver)

        //setting broadcast
        this.broadcastReceiver = this.getBroadcastReceiver()
        this.context!!.registerReceiver(
                this.broadcastReceiver,
                IntentFilter(this.id))

        //Check if alarm is already running
        when (this.alarmType) {
            AlarmType.REPEAT -> this.alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ensurePositiveTime, this.pendingIntent)
            AlarmType.ONE_TIME -> this.alarmManager!!.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ensurePositiveTime, this.pendingIntent)
        }

        Log.e("Alarm", "Alarm updated..!")

    }

    //Listeners:------------------------------------------------------------------------------------
    @Synchronized
    fun addListener(alarmListener: AlarmListener?) {
        if (alarmListener == null) {
            throw IllegalStateException("Listener can't be null!")
        }

        this.alarmListenerSet!!.add(alarmListener)
    }

    @Synchronized
    fun removeListener(alarmListener: AlarmListener?) {
        if (alarmListener == null) {
            throw IllegalStateException("Listener can't be null!")
        }

        this.alarmListenerSet!!.remove(alarmListener)
    }

    //Broadcast Receiver:---------------------------------------------------------------------------
    private fun getBroadcastReceiver(): BroadcastReceiver {

        return object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {

                for (alarmListener in this@AlarmBuilder.alarmListenerSet!!) {
                    alarmListener.perform(context, intent)
                }
            }
        }
    }
}