package com.studiolkj.myencnote.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [MemoData::class], version = 1)
abstract class MemoDatabase: RoomDatabase() {
    abstract fun getDB(): MemoDao

    companion object {
        private val DB_NAME = "my_memo_db_v01"
        private var instance: MemoDatabase? = null

        fun getInstance(context: Context): MemoDatabase {
            return instance?: synchronized(this){
                instance?: buildDatabase((context))
            }
        }

        private fun buildDatabase(context: Context): MemoDatabase{
            return Room.databaseBuilder(context.applicationContext,
                MemoDatabase::class.java,
                DB_NAME)
                .addCallback(object : RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).build()

        }
    }

}