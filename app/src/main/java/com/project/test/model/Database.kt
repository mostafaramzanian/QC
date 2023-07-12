package com.project.test.model

import android.app.Activity
import android.app.Application
import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import java.lang.ref.WeakReference

import java.sql.SQLException

/*
class Database(context: Context?) :
    SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "QC.db"
        private const val DATABASE_VERSION = 1
    }
}


 */

class Database(context: Application) :
    SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "QC.db"
        private const val DATABASE_VERSION = 1
        private var instance: WeakReference<Database>? = null

        @Synchronized
        fun getInstance(context: Application): Database {
            var database = instance?.get()
            if (database == null) {
                database = Database(context)
                instance = WeakReference(database)
            }
            return database
        }
    }
    var companion = Companion
}
