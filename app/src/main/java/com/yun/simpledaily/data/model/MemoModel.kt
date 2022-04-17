package com.yun.simpledaily.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yun.simpledaily.base.Item

@Entity(tableName = "Memo")
data class MemoModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID") val id: Long = 0,
    @ColumnInfo(name = "TITLE") val title: String = "",
    @ColumnInfo(name = "MEMO") val memo: String = ""
)

data class MemoModels(
    override var id: Int = 0,
    override var viewType: Int = 0,
    val title: String = "",
    val memo: String = ""
) : Item()
