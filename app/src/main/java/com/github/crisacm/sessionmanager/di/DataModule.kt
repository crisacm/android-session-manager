package com.github.crisacm.sessionmanager.di

import com.github.crisacm.module.sessionmanager.SessionManager
import com.github.crisacm.module.sessionmanager.SessionManagerInstance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseAuth = Firebase.auth
val appIoDispatcher: CoroutineDispatcher = Dispatchers.IO

val dataModule = module {
    singleOf(::firebaseAuth)
    singleOf(::appIoDispatcher)
    single<SessionManager> { SessionManagerInstance(androidContext()) }
}
