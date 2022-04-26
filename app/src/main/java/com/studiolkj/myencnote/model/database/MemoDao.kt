package com.studiolkj.myencnote.model.database

import androidx.room.*

@Dao
interface MemoDao {
    @Query("SELECT * FROM ${MemoData.MEMO_TABLE_NAME} ORDER BY `editedAt` DESC")
    fun getAll(): MutableList<MemoData>

    @Query("SELECT COUNT('index') FROM ${MemoData.MEMO_TABLE_NAME}")
    fun getCount(): Int

    @Query("SELECT * FROM ${MemoData.MEMO_TABLE_NAME} WHERE `editedAt` < :offset ORDER BY `editedAt` DESC LIMIT :limit")
    fun getMemoDataList(offset: Long, limit: Int): MutableList<MemoData>

    @Query("SELECT * FROM ${MemoData.MEMO_TABLE_NAME} WHERE `index` = :index ORDER BY `editedAt` DESC")
    fun getMemoData(index: Int): MemoData

    @Query("DELETE FROM ${MemoData.MEMO_TABLE_NAME} WHERE `index` = :index")
    fun deleteMemo(index: Int)

    @Insert
    fun insertMemoData(data: MemoData)

    @Update
    fun updateMemoData(data: MemoData)

    @Delete
    fun deleteMemoData(data: MemoData)

}