package com.example.calendariolaboral_v40.core.di

import com.example.calendariolaboral_v40.core.data.repositoryImpl.VacacionesRepositoryImpl
import com.example.calendariolaboral_v40.modulos.vacaciones.domain.repository.VacacionesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract  class VacacionesModulo {
    @Binds
    @Singleton
    abstract fun bindVacacionesRepository(
        vacacionesRepositoryImpl: VacacionesRepositoryImpl
    ): VacacionesRepository
}