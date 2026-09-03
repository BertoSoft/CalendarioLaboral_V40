package com.example.calendariolaboral_v40.core.di

import com.example.calendariolaboral_v40.core.data.repositoryImpl.FestivosRepositoryImpl
import com.example.calendariolaboral_v40.modulos.festivos.domain.repository.FestivosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class FestivosModulo {

    @Binds
    @Singleton
    abstract fun bindFestivosRepository(
        festivosRepositoryImpl: FestivosRepositoryImpl
    ): FestivosRepository
}