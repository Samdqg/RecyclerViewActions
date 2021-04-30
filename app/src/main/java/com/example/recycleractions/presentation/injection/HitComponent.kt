package com.example.recycleractions.presentation.injection

import com.example.recycleractions.presentation.features.list.HitViewModel
import dagger.Component

@Component(modules = [HitModule::class])
interface HitComponent {
    fun inject(viewModel: HitViewModel)
}