package com.example.blackjackappx

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context : Context?) : SQLiteOpenHelper(context, "UserDatabase.db", null, 1) {

    val USER_TABLE = "USER_TABLE"
    val COLUMN_EMAIL = "COLUMN_EMAIL"
    val COLUMN_PASSWORD = "COLUMN_PASSWORD"

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE " +
                USER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //används ej
    }

    fun isInDatabase(email : String, password : String) : Boolean {
        val db : SQLiteDatabase = this.readableDatabase
        val query = "SELECT * FROM $USER_TABLE WHERE $COLUMN_EMAIL = ? " +
                "AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        return cursor.count != 0
    }

    fun initiateDatabase() {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_EMAIL, "user@testuser.com")
        cv.put(COLUMN_PASSWORD, "Password123")

        val long = db.insert(USER_TABLE, null, cv)
    }
}