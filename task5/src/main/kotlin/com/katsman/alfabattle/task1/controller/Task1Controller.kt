package com.katsman.alfabattle.task1.controller

import com.katsman.alfabattle.task1.dto.CheckDto
import com.katsman.alfabattle.task1.dto.FinalPriceReceipt
import com.katsman.alfabattle.task1.dto.PromoMatrix
import com.katsman.alfabattle.task1.dto.ShoppingCart
import com.katsman.alfabattle.task1.service.Task1Service
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("")
class Task1Controller(private val task1Service: Task1Service) {

    @PostMapping("/promo", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun promo(@RequestBody promoMatrix: PromoMatrix) {
        task1Service.promo(promoMatrix)
    }

    @PostMapping("/receipt", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun receipt(@RequestBody shoppingCart: ShoppingCart): ResponseEntity<FinalPriceReceipt> {
        return ResponseEntity.ok(task1Service.receipt(shoppingCart))
    }

    @GetMapping("/check", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun check(): ResponseEntity<CheckDto> {
        return ResponseEntity.ok(CheckDto(true))
    }
}