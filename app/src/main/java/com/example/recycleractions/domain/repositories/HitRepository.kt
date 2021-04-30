package com.example.recycleractions.domain.repositories

import com.example.recycleractions.domain.entities.HitInput
import com.example.recycleractions.domain.entities.HitResponse

interface HitRepository {

    suspend fun getHits(hitInput: HitInput): HitResponse
}