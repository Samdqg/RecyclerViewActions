package com.example.recycleractions.presentation.features.list

import com.example.recycleractions.domain.entities.Hit

interface HitListener {
    fun onclick(hit: Hit)
    fun onDelete(hit: Hit)
}