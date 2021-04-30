package com.example.recycleractions.domain.entities

class HitResponse (
    val hits: List<Hit>,
    val page: Int,
    val hitsPerPage: Int
)