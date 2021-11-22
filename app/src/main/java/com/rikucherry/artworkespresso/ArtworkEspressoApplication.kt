package com.rikucherry.artworkespresso

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class ArtworkEspressoApplication : Application() {
    val state = UUID.randomUUID().toString()
}