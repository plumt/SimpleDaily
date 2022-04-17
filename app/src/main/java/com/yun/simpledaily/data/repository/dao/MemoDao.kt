package com.yun.simpledaily.data.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yun.simpledaily.data.model.MemoModel

@Dao
abstract class MemoDao {
    @Query("SELECT * FROM Memo")
    abstract fun selectMemo(): List<MemoModel>

//    @Query("SELECT * FROM CALENDAR WHERE DATE >= :sDt AND DATE <= :eDt ORDER BY DATE")
//    abstract fun selectMonth(sDt: Long, eDt: Long): List<CalendarModel>

//    @Query("DELETE FROM Calendar WHERE DATE == :dt AND EVENT == :event")
//    abstract fun deleteEvent(dt: Long, event: String)

    @Insert
    abstract fun insertMemo(memoModel: MemoModel): Long
}