package com.lans.composeresult.hilt.simple

import com.lans.composeresult.core.ResultStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ResultModule {
    @Provides
    @Singleton
    fun provideResultStore(): ResultStore = ResultStore()
}