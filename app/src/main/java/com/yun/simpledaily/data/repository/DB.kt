package com.yun.simpledaily.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yun.simpledaily.data.model.CalendarModel
import com.yun.simpledaily.data.model.MemoModel
import com.yun.simpledaily.data.repository.dao.CalendarDao
import com.yun.simpledaily.data.repository.dao.MemoDao

@Database(entities = [CalendarModel::class, MemoModel::class], version = 2, exportSchema = true)
abstract class DB : RoomDatabase() {
    abstract fun calendarDao(): CalendarDao
    abstract fun memoDao(): MemoDao

}