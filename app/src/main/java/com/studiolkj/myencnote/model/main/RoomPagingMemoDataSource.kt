package com.studiolkj.myencnote.model.main

import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
import com.studiolkj.myencnote.model.database.MemoDao
import com.studiolkj.myencnote.model.database.MemoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RoomPagingMemoDataSource private constructor(val database: MemoDao): ItemKeyedDataSource<Long, MemoData>(){

    companion object{
        val TAG = "RoomPagingMemoDataSource"
    }

    class Factory(val database: MemoDao): DataSource.Factory<Long, MemoData>(){
        override fun create(): DataSource<Long, MemoData> {
            return RoomPagingMemoDataSource(database)
        }
    }

    var itemKey = 0L
    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<MemoData>
    ) {
        GlobalScope.launch(Dispatchers.IO) {
//            MyLog.debug(TAG, "loadInitial(${System.currentTimeMillis()}) start")
            val memoList = database.getMemoDataList(System.currentTimeMillis(), params.requestedLoadSize)
            if(memoList.size > 0) {
                itemKey = memoList.get(memoList.size - 1).editedAt
            }
//            for(book in storyBookList){
//                MyLog.debug(TAG, "   - Index: ${book.index}, Title: ${book.title}, editedAt: ${book.editedAt}, , createdAt: ${book.createdAt}")
//            }
//            MyLog.debug(TAG, "loadInitial(key: 0, limit: ${params.requestedLoadSize}) => load size: ${storyBookList.size}, itemKey: ${itemKey}")
            callback.onResult(memoList)
        }
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<MemoData>) {
        GlobalScope.launch(Dispatchers.IO) {
//            MyLog.debug(TAG, "loadAfter(${itemKey}) start")
            val memoList = database.getMemoDataList(params.key, params.requestedLoadSize)
            if(memoList.size > 0) {
                itemKey = memoList.get(memoList.size - 1).editedAt
            }
//            for(book in storyBookList){
//                MyLog.debug(TAG, "   - Index: ${book.index}, Title: ${book.title}, editedAt: ${book.editedAt}, , createdAt: ${book.createdAt}")
//            }
//            MyLog.debug(TAG, "loadAfter(key: ${params.key}, limit: ${params.requestedLoadSize}) => load size: ${storyBookList.size}, itemKey: ${itemKey}")
            callback.onResult(memoList)
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<MemoData>) {

    }

    override fun getKey(item: MemoData): Long {
        return itemKey
    }

}