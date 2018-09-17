package com.zubair.alarmmanager

import android.app.Application

import timber.log.Timber

class ApplicationController : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
