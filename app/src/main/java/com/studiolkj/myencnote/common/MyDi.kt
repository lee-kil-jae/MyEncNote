package com.studiolkj.myencnote.common

import com.studiolkj.myencnote.model.database.MemoData
import com.studiolkj.myencnote.model.database.MemoDatabase
import com.studiolkj.myencnote.viewModel.AddMemoViewModel
import com.studiolkj.myencnote.viewModel.EditMemoViewModel
import com.studiolkj.myencnote.viewModel.MainViewModel
import com.studiolkj.myencnote.viewModel.ViewMemoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var dbPart = module {
    factory { MemoDatabase.getInstance(get()).getDB() }
}

var viewModelPart = module {
    viewModel {
        MainViewModel(get())
    }

    viewModel {
        AddMemoViewModel(get())
    }

    viewModel {
        (index: Int, password: String) ->
        ViewMemoViewModel(index, password, get())
    }

    viewModel {
            (index: Int, password: String) ->
        EditMemoViewModel(index, password, get())
    }
}

var myDiModule = listOf(
    dbPart,
    viewModelPart
)
