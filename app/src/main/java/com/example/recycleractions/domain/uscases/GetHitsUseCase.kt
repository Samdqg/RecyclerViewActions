package com.example.recycleractions.domain.uscases

import com.example.recycleractions.domain.entities.HitInput
import com.example.recycleractions.domain.entities.HitResponse
import com.example.recycleractions.domain.repositories.HitRepository
import com.example.recycleractions.domain.uscases.base.UseCase

class GetHitsUseCase(private val hitRepository: HitRepository): UseCase<HitResponse, HitInput>() {
    override suspend fun executeOnBackground(input: HitInput): HitResponse {
        return hitRepository.getHits(input)
    }
}