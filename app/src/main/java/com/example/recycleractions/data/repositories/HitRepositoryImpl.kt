package com.example.recycleractions.data.repositories

import com.example.recycleractions.data.retrofit.HitsApiService
import com.example.recycleractions.data.retrofit.RetrofitBase
import com.example.recycleractions.domain.entities.HitInput
import com.example.recycleractions.domain.entities.HitResponse
import com.example.recycleractions.domain.repositories.HitRepository
import retrofit2.create

class HitRepositoryImpl: HitRepository {

    private val retrofit = RetrofitBase.getRetrofitInstance()
    private val productApiService = retrofit.create<HitsApiService>()

    override suspend fun getHits(hitInput: HitInput): HitResponse {
        return productApiService.getHits(hitInput.query, hitInput.page)
    }
}