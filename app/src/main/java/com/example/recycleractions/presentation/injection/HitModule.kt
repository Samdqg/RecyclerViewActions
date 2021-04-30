package com.example.recycleractions.presentation.injection

import com.example.recycleractions.data.repositories.HitRepositoryImpl
import com.example.recycleractions.domain.repositories.HitRepository
import com.example.recycleractions.domain.uscases.GetHitsUseCase
import dagger.Module
import dagger.Provides

@Module
class HitModule {

    @Provides
    fun provideHitRepository(): HitRepository{
        return HitRepositoryImpl()
    }

    @Provides
    fun provideGetHitsUseCase(hitRepository: HitRepository): GetHitsUseCase{
        return GetHitsUseCase(hitRepository)
    }
}