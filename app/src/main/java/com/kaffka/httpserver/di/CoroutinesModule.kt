package com.kaffka.httpserver.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    @Provides
    fun provideGlobalCoroutineScope(): CoroutineScope = GlobalScope

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
