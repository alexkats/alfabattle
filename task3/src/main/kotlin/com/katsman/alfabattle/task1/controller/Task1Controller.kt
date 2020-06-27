package com.katsman.alfabattle.task1.controller

import com.katsman.alfabattle.task1.dto.BranchWithDistance
import com.katsman.alfabattle.task1.dto.BranchWithPrediction
import com.katsman.alfabattle.task1.dto.ErrorDto
import com.katsman.alfabattle.task1.service.Task1Service
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("")
class Task1Controller(private val task1Service: Task1Service) {

    @GetMapping("/branches/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBranchInfoById(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val result = task1Service.getBranchInfoById(id)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity(ErrorDto("branch not found"), HttpStatus.NOT_FOUND)
    }

    @GetMapping("/branches", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getNearestBranch(
        @RequestParam("lon") lon: String,
        @RequestParam("lat") lat: String
    ): ResponseEntity<BranchWithDistance> {
        return ResponseEntity.ok(task1Service.getNearestBranch(lon, lat))
    }

    @GetMapping("/branches/{id}/predict", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun predict(
        @PathVariable("id") id: Long,
        @RequestParam("dayOfWeek") dayOfWeek: Int,
        @RequestParam("hourOfDay") hourOfDay: Int
    ): ResponseEntity<Any> {
        val result = task1Service.predict(id, dayOfWeek, hourOfDay)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity(ErrorDto("branch not found"), HttpStatus.NOT_FOUND)
    }
}