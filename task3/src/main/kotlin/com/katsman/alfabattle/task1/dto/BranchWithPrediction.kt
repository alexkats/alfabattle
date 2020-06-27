package com.katsman.alfabattle.task1.dto

data class BranchWithPrediction(
    val id: Long,
    val title: String,
    val lon: String,
    val lat: String,
    val address: String,
    val dayOfWeek: Int,
    val hourOfDay: Int,
    val predicting: Long
)