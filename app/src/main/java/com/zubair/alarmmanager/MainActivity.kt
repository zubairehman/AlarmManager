package com.zubair.alarmmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.zubair.alarmmanager.builder.AlarmBuilder
import com.zubair.alarmmanager.enums.AlarmType
import com.zubair.alarmmanager.interfaces.AlarmListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), AlarmListener {

    //Alarm builder
    var builder: AlarmBuilder? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //creating alarm builder
        builder = AlarmBuilder().with(this)
                .setTimeInMilliSeconds(TimeUnit.SECONDS.toMillis(10))
                .setId("UPDATE_INFO_SYSTEM_SERVICE")
                .setAlarmType(AlarmType.REPEAT)
                .build()

        //setting click listeners
        btnSetAlarm.setOnClickListener {
            builder?.setAlarm()
        }

        btnCancelAlarm.setOnClickListener {
            builder?.cancelAlarm()
        }
    }

    override fun onResume() {
        super.onResume()
        builder?.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        builder?.removeListener(this)
    }

    override fun perform(context: Context, intent: Intent) {
        Log.i("Alarm", "Do your work here")
    }
}
