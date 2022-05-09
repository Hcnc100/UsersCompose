package com.nullpointer.userscompose.inject

import com.nullpointer.userscompose.domain.users.UserRepoImpl
import com.nullpointer.userscompose.domain.users.UsersRepository
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
    abstract fun userRepository(repositoryImpl: UserRepoImpl): UsersRepository
}
