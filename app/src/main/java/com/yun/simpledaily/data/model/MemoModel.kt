package com.yun.simpledaily.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yun.simpledaily.base.Item

@Entity(tableName = "Memo")
data class MemoModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID") var id: Long = 0,
    @ColumnInfo(name = "TITLE") var title: String = "",
    @ColumnInfo(name = "MEMO") var memo: String = ""
)

data class MemoModels(
    override var id: Int = 0,
    override var viewType: Int = 0,
    var id_: Long,
    var title: String = "",
    var memo: String = "",
    var ripple: Boolean = false,
    var last: Int = -1
) : Item()
