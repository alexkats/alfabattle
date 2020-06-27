package com.katsman.alfabattle.task1.dto

data class BranchWithDistance(
    val id: Long,
    val title: String,
    val lon: String,
    val lat: String,
    val address: String,
    val distance: Long
)