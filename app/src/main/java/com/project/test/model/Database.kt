package com.project.test.model

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper


class Database(context: Context?) :
    SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "QC.db"
        private const val DATABASE_VERSION = 1
    }
}