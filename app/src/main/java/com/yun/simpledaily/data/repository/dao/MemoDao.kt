package com.yun.simpledaily.data.repository.dao

import androidx.room.*
import com.yun.simpledaily.data.model.MemoModel

@Dao
abstract class MemoDao {
    @Query("SELECT * FROM Memo ORDER BY ID DESC")
    abstract fun selectMemo(): List<MemoModel>

    @Query("SELECT * FROM Memo ORDER BY ID DESC LIMIT 3")
    abstract fun selectMemo3(): List<MemoModel>

//    @Query("SELECT * FROM CALENDAR WHERE DATE >= :sDt AND DATE <= :eDt ORDER BY DATE")
//    abstract fun selectMonth(sDt: Long, eDt: Long): List<CalendarModel>

    @Query("DELETE FROM Memo WHERE ID == :id")
    abstract fun deleteMemo(id: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateMemo(vararg memo: MemoModel) : Int

    @Insert
    abstract fun insertMemo(memoModel: MemoModel): Long
}