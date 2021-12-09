package com.compose.sultan.plagiarismchecker.modules

import com.compose.sultan.plagiarismchecker.repo.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object ActivityModule{
    @Provides
    fun providesRepoObject():Repository{
        return Repository.getInstance()
    }
}

