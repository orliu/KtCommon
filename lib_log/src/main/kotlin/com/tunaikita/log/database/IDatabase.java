package com.tunaikita.log.database;

import android.database.sqlite.SQLiteDatabase;

public interface IDatabase {
        void onCreate(SQLiteDatabase db);

        void onUpgrade(SQLiteDatabase db);

        void onOpen(SQLiteDatabase db);
    }