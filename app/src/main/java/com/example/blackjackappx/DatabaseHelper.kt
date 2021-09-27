package com.example.blackjackappx

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.blackjackappx.entities.User

class DatabaseHelper(context : Context?) : SQLiteOpenHelper(context, "UserDatabase.db", null, 1) {

    private val USER_TABLE = "USER_TABLE"
    private val COLUMN_USERNAME = "COLUMN_USERNAME"
    private val COLUMN_PASSWORD = "COLUMN_PASSWORD"
    private val COLUMN_STACK = "COLUMN_STACK"

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE " +
                USER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_STACK + " INTEGER)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //används ej
    }

    fun saveUserData(user : User) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_STACK, user.userStack)

        db.update(USER_TABLE, cv, "COLUMN_USERNAME = ?", arrayOf(user.username) )
    }

    fun isInDatabase(username : String, password : String) : Boolean {
        val db : SQLiteDatabase = this.readableDatabase
        val query = "SELECT * FROM $USER_TABLE WHERE $COLUMN_USERNAME = ? " +
                "AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        return cursor.count != 0
    }

    fun initiateDatabase() {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_USERNAME, "Testuser")
        cv.put(COLUMN_PASSWORD, "Password123")

        db.insert(USER_TABLE, null, cv)
    }
}