package com.studiolkj.myencnote.common

import com.studiolkj.myencnote.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelPart = module {
    viewModel {
        MainViewModel()
    }
}

var myDiModule = listOf(
    viewModelPart
)
