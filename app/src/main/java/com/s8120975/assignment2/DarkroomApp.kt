package com.s8120975.assignment2

import android.app.Application
import com.s8120975.assignment2.di.studioModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Application class. Note: Koin, dependency injection, startKoin.
 * onCreate is the function that starts Koin. The bindings are studioModule in StudioModule.kt.
 * The manifest points android:name at this class.
 */
class DarkroomApp : Application() {
    /** Function that starts dependency injection before any screen is shown. */
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DarkroomApp)
            modules(studioModule)
        }
    }
}
