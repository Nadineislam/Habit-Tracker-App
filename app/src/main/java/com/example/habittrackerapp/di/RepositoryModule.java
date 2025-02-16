package com.example.habittrackerapp.di;

import com.example.habittrackerapp.data.repository.HabitRepositoryImpl;
import com.example.habittrackerapp.domain.repository.HabitRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {
    @Binds
    @Singleton
    public abstract HabitRepository bindHabitRepository(HabitRepositoryImpl habitRepositoryImpl);
}
