package com.rikucherry.artworkespresso

import android.app.Application
import com.rikucherry.artworkespresso.common.tool.GlobalResponseOperator
import com.skydoves.sandwich.SandwichInitializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.*

@HiltAndroidApp
class ArtworkEspressoApplication : Application() {
    val state = UUID.randomUUID().toString()

    override fun onCreate() {
        super.onCreate()

        //initialize global sandwich operator
        SandwichInitializer.sandwichOperator = GlobalResponseOperator<Any>(this)

        //initialize timber
        initializeTimber()
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}