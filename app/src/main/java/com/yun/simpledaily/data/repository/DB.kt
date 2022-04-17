package com.yun.simpledaily.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yun.simpledaily.data.model.MemoModel
import com.yun.simpledaily.data.repository.dao.MemoDao

@Database(entities = [MemoModel::class], version = 1, exportSchema = true)
abstract class DB : RoomDatabase() {
    abstract fun memoDao(): MemoDao

}