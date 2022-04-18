package com.yun.simpledaily.data.repository.dao

import androidx.room.*
import com.yun.simpledaily.data.model.MemoModel

@Dao
abstract class MemoDao {
    @Query("SELECT * FROM Memo")
    abstract fun selectMemo(): List<MemoModel>

    @Query("SELECT * FROM Memo ORDER BY ID DESC LIMIT 5")
    abstract fun selectMemo5(): List<MemoModel>

//    @Query("SELECT * FROM CALENDAR WHERE DATE >= :sDt AND DATE <= :eDt ORDER BY DATE")
//    abstract fun selectMonth(sDt: Long, eDt: Long): List<CalendarModel>

//    @Query("DELETE FROM Calendar WHERE DATE == :dt AND EVENT == :event")
//    abstract fun deleteEvent(dt: Long, event: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateMemo(vararg memo: MemoModel) : Int

    @Insert
    abstract fun insertMemo(memoModel: MemoModel): Long
}