package com.katsman.alfabattle.task1.dto

import java.math.BigDecimal

data class Item(
    val id: String,
    val name: String,
    val groupId: String,
    val price: BigDecimal
)