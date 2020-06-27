package com.katsman.alfabattle.task1.service

import com.katsman.alfabattle.task1.dto.BranchWithDistance
import com.katsman.alfabattle.task1.dto.BranchWithPrediction
import com.katsman.alfabattle.task1.entity.Branch

interface Task1Service {

    fun getBranchInfoById(id: Long): Branch?

    fun getNearestBranch(lon: String, lat: String): BranchWithDistance

    fun predict(branchId: Long, dayOfWeek: Int, hourOfDay: Int): BranchWithPrediction?
}