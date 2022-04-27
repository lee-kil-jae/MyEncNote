package com.studiolkj.myencnote.common

import com.studiolkj.myencnote.model.database.MemoData
import com.studiolkj.myencnote.model.database.MemoDatabase
import com.studiolkj.myencnote.viewModel.*
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

    viewModel {
        SettingViewModel()
    }
}

var myDiModule = listOf(
    dbPart,
    viewModelPart
)
