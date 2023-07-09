package com.project.test.model

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
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
class Database(private val context: Context) :
    SQLiteAssetHelper(context.applicationContext, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "QC.db"
        private const val DATABASE_VERSION = 1
    }

    @Volatile
    private var instance: Database? = null
    fun getInstance(): Database =
        instance ?: synchronized(this) {
            instance ?: Database(context.applicationContext).also { instance = it }
        }

}

//object DatabaseConnection {
//
//    fun getDB(context: Context): Database? {
//        var dbase: Database? = null
//        if (dbase == null) {
//            try {
//                dbase = Database(context)
//            } catch (e: SQLException) {
//                println(e.message)
//            }
//        }
//        return dbase
//    }
//}
//
