package com.github.crisacm.sessionmanager.di

import com.github.crisacm.sessionmanager.presentation.screens.home.HomeViewModel
import com.github.crisacm.sessionmanager.presentation.screens.login.LoginViewModel
import com.github.crisacm.sessionmanager.presentation.screens.register.RegisterViewModel
import com.github.crisacm.sessionmanager.presentation.screens.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), androidContext(), get()) }
    viewModel { RegisterViewModel() }
    viewModel { SplashViewModel() }
    viewModel { HomeViewModel() }
}
