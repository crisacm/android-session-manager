package com.github.crisacm.sessionmanager.di

import com.github.crisacm.module.sessionmanager.SessionManagerProvider
import com.github.crisacm.module.sessionmanager.auth.AuthenticationManager
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.sessionmanager.auth.AuthenticationManagerImp
import com.github.crisacm.sessionmanager.data.repo.AuthRepository
import com.github.crisacm.sessionmanager.data.repo.AuthRepositoryImp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseAuth = Firebase.auth
val appIoDispatcher: CoroutineDispatcher = Dispatchers.IO

val dataModule = module {
    singleOf(::firebaseAuth)
    singleOf(::appIoDispatcher)
    single<AuthenticationManager> { AuthenticationManagerImp() }
    single<SessionManager> { SessionManagerProvider.getInstance(get(), get()) }
    single<AuthRepository> { AuthRepositoryImp(get(), get()) }
}
