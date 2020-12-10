package com.fastnews

import android.app.Application
import com.fastnews.service.model.realm.PostResponseEntity
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*

/**
 * MyApplication
 */
class MyApplication : Application() {

    //region superclass
    override fun onCreate() {
        super.onCreate()
        configRealm()
        cleanOldRealmData()
    }
//endregion

    /*** Config Realm app */
    private fun configRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("myrealm.realm").build()
        Realm.setDefaultConfiguration(config)
    }

    /*** After 6 days, remove on Realm data */
    private fun cleanOldRealmData() {
        val realm = Realm.getDefaultInstance()
        val backupDays = -6

        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, backupDays)
        val finalDate: Date = cal.time

        realm.beginTransaction()
        realm.where(PostResponseEntity::class.java)
            .lessThan("date", finalDate)
            .findAll()
            .deleteAllFromRealm()
        realm.commitTransaction()
    }
}