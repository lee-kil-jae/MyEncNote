package com.studiolkj.myencnote.model.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.studiolkj.myencnote.model.database.MemoData.Companion.INDEX
import com.studiolkj.myencnote.model.database.MemoData.Companion.MEMO_TABLE_NAME
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = MEMO_TABLE_NAME,
    indices = [Index(value = [INDEX])])
@Parcelize
data class MemoData(@ColumnInfo(name = "hasEnc") var hasEnc: Boolean = true,
                    @ColumnInfo(name = "hint") var hint: String = "",
                    @ColumnInfo(name = "editedAt") var editedAt: Long = 0L,
                    @ColumnInfo(name = "backgroundId") var backgroundId: Int = 0,
                    @ColumnInfo(name = "openData") var openData: String = "",
                    @ColumnInfo(name = "encData") var encData: ByteArray?
): Parcelable {
    @PrimaryKey(autoGenerate = true) var index: Int = 0

    constructor() : this(true, "", 0L, 0, "", null)
    companion object{
        const val MEMO_TABLE_NAME = "tableMemo"
        const val INDEX = "index"
    }
}