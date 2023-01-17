package com.taitos.testpjk

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.taitos.testpjk.view.activityDesc.ActivityViewModel
import com.taitos.testpjk.view.bill.BillViewModel
import com.taitos.testpjk.view.working.WorkingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        initCoin()

    }

    fun initCoin() {
        startKoin {
            androidContext(this@App)

            val model = module {
                viewModel { ActivityViewModel() }
                viewModel { WorkingViewModel() }
                viewModel { BillViewModel() }
            }

            modules( model)

        }

    }

}