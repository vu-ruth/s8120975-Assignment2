package com.s8120975.assignment2.screens.catalogue

import com.s8120975.assignment2.model.Technique

data class CatalogueState(
    val techniques: List<Technique> = emptyList(),
    val entityTotal: Int = 0,
    val busy: Boolean = false,
    val problem: String? = null,
)
