package com.github.crisacm.sessionmanager.di

import com.github.crisacm.sessionmanager.ui.feature.home.HomeViewModel
import com.github.crisacm.sessionmanager.ui.feature.login.LoginViewModel
import com.github.crisacm.sessionmanager.ui.feature.register.RegisterViewModel
import com.github.crisacm.sessionmanager.ui.feature.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get(), get(), androidContext(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}
