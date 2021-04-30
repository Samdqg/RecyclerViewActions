package com.example.recycleractions.presentation.features.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recycleractions.domain.entities.HitInput
import com.example.recycleractions.domain.entities.HitResponse
import com.example.recycleractions.domain.uscases.GetHitsUseCase
import com.example.recycleractions.presentation.injection.DaggerHitComponent
import com.example.recycleractions.presentation.injection.HitComponent
import com.example.recycleractions.presentation.injection.HitModule
import java.lang.Exception
import javax.inject.Inject

class HitViewModel: ViewModel() {

    private val hitComponent: HitComponent = DaggerHitComponent
        .builder()
        .hitModule(HitModule())
        .build()

    init {
        hitComponent.inject(this)
    }

    @Inject
    lateinit var getHitsUseCase: GetHitsUseCase

    private val hitResponse: MutableLiveData<HitResponse> = MutableLiveData()
    private val exception: MutableLiveData<Exception> = MutableLiveData()

    fun getHitResponse(): LiveData<HitResponse> {
        return hitResponse
    }

    fun getException(): LiveData<Exception> {
        return exception
    }

    fun getHits(hitInput: HitInput){
        getHitsUseCase.execute({
            onComplete {
                hitResponse.value = it
            }
            onError {
                exception.value = it
            }
        }, hitInput)
    }

}