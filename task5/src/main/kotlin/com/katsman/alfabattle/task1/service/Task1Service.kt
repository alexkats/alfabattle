package com.katsman.alfabattle.task1.service

import com.katsman.alfabattle.task1.dto.FinalPriceReceipt
import com.katsman.alfabattle.task1.dto.PromoMatrix
import com.katsman.alfabattle.task1.dto.ShoppingCart

interface Task1Service {

    fun promo(promoMatrix: PromoMatrix)

    fun receipt(shoppingCart: ShoppingCart): FinalPriceReceipt
}