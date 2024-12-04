package com.github.crisacm.sessionmanager.di

import com.github.crisacm.module.sessionmanager.SessionManagerProvider
import com.github.crisacm.module.sessionmanager.core.SessionManager
import com.github.crisacm.sessionmanager.data.repo.AuthRepository
import com.github.crisacm.sessionmanager.data.repo.AuthRepositoryImp
import com.github.crisacm.sessionmanager.util.crypto.CryptHelper
import com.github.crisacm.sessionmanager.util.crypto.CryptoHelperImp
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
    single<SessionManager> { SessionManagerProvider.getInstance(get()) }
    single<AuthRepository> { AuthRepositoryImp(get(), get()) }
    single<CryptHelper> { CryptoHelperImp(get()) }
}
