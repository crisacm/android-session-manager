package com.github.crisacm.sessionmanager.di

import com.github.crisacm.sessionmanager.presentation.screens.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LoginViewModel()
    }
}
