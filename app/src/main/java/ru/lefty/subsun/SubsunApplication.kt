package ru.lefty.subsun

import android.app.Application
import ru.lefty.subsun.di.AppContainer
import ru.lefty.subsun.di.AppContainerImpl

class SubsunApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
