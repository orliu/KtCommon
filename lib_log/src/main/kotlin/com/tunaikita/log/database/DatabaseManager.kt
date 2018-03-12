package com.tunaikita.log.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.orliu.kotlin.common.tools.Logger
import com.tunaikita.log.bean.HttpLog

/**
 * sqlite管理
 * Created by orliu on 17/11/29.
 */
object DatabaseManager {

    private val databaseName = "HttpLog.db"
    private val databaseVersion = 1

    fun initDatabase(context: Context) {

        val dbFile = context.getDatabasePath(databaseName)
        //Logger.v("log db path: ${dbFile.absolutePath}")
        when (dbFile.exists()) {
            true -> openDatabase(context)
            false -> createDatabase(context)
        }
    }

    /**
     * create
     */
    private fun createDatabase(context: Context) {
        Database.getInstance().apply {
            createDatabase(context, databaseName, databaseVersion, object : IDatabase{
                override fun onCreate(db: SQLiteDatabase) {
                    Logger.v("create database $databaseName success")
                    if (createTable(HttpLog::class.java)) {
                        //Logger.v("create table ${HttpLog::class.java.simpleName} success")
                    } else {
                        //Logger.e("create table ${HttpLog::class.java.simpleName} error")
                    }
                }

                override fun onUpgrade(db: SQLiteDatabase?) {
                }

                override fun onOpen(db: SQLiteDatabase?) {
                }
            })
        }
    }

    private fun openDatabase(context: Context) {
        Database.getInstance().openDatabase(context, databaseName, databaseVersion, object : IDatabase{
            override fun onOpen(db: SQLiteDatabase) {
                //Logger.v("open database $databaseName success")
            }

            override fun onCreate(db: SQLiteDatabase?) {
            }

            override fun onUpgrade(db: SQLiteDatabase?) {
            }
        })
    }
}