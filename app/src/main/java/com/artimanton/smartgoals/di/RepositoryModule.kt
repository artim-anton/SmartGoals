package com.artimanton.smartgoals.di

import com.artimanton.smartgoals.data.repository.GoalRepositoryImpl
import com.artimanton.smartgoals.data.repository.SessionRepositoryImpl
import com.artimanton.smartgoals.data.repository.TaskRepositoryImpl
import com.artimanton.smartgoals.domain.repository.GoalRepository
import com.artimanton.smartgoals.domain.repository.SessionRepository
import com.artimanton.smartgoals.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindGoalRepository(
        impl: GoalRepositoryImpl
    ): GoalRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(
        impl: SessionRepositoryImpl
    ): SessionRepository
}