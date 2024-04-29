package com.github.crisacm.sessionmanager.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.crisacm.sessionmanager.ui.screens.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LoginViewModel()
    }
}
