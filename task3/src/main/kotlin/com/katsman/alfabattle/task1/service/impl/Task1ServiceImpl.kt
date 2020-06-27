package com.katsman.alfabattle.task1.service.impl

import com.katsman.alfabattle.task1.dto.BranchWithDistance
import com.katsman.alfabattle.task1.dto.BranchWithPrediction
import com.katsman.alfabattle.task1.entity.Branch
import com.katsman.alfabattle.task1.repository.BranchRepository
import com.katsman.alfabattle.task1.repository.QueueLogRepository
import com.katsman.alfabattle.task1.service.Task1Service
import org.apache.commons.math3.stat.descriptive.rank.Median
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sin
import kotlin.math.sqrt

@Service
class Task1ServiceImpl(
    private val branchRepository: BranchRepository,
    private val queueLogRepository: QueueLogRepository
) : Task1Service {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Task1ServiceImpl::class.java)

        private const val EARTH_RADIUS = 6371.0
        private const val KM_MULTIPLIER = 1_000
    }

    override fun getBranchInfoById(id: Long): Branch? {
        LOGGER.info(id.toString())
        return branchRepository.findByIdOrNull(id)
    }

    override fun getNearestBranch(lon: String, lat: String): BranchWithDistance {
        var result: BranchWithDistance? = null

        branchRepository.findAll().forEach { branch ->
            val distance = calcDistance(lon.toDouble(), lat.toDouble(), branch.lon!!.toDouble(), branch.lat!!.toDouble())

            if ((result?.distance ?: Long.MAX_VALUE) > distance) {
                result = BranchWithDistance(
                    id = branch.id!!,
                    title = branch.title!!,
                    lon = branch.lon!!,
                    lat = branch.lat!!,
                    address = branch.address!!,
                    distance = distance.roundToLong()
                )
            }
        }

        return result!!
    }

    override fun predict(branchId: Long, dayOfWeek: Int, hourOfDay: Int): BranchWithPrediction? {
        val branch = branchRepository.findByIdOrNull(branchId) ?: return null
        val logs = queueLogRepository.findAllByBranch(branch)

        val waitingTimes = logs.asSequence()
            .filter { it.data!!.dayOfWeek.value == dayOfWeek }
            .filter { it.endTimeOfWait!!.hour == hourOfDay }
            .map { it.endTimeOfWait!!.toSecondOfDay() - it.startTimeOfWait!!.toSecondOfDay() }
            .sorted()
            .toList()

        val median = Median()
        median.data = waitingTimes.map { it.toDouble() }.toDoubleArray()
        val predicting = median.evaluate().roundToInt()

        return BranchWithPrediction(
            id = branchId,
            title = branch.title!!,
            lon = branch.lon!!,
            lat = branch.lat!!,
            address = branch.address!!,
            dayOfWeek = dayOfWeek,
            hourOfDay = hourOfDay,
            predicting = predicting.toLong()
        )
    }

    private fun calcDistance(lon1: Double, lat1: Double, lon2: Double, lat2: Double): Double {
        val lon1Rad = Math.toRadians(lon1)
        val lat1Rad = Math.toRadians(lat1)
        val lon2Rad = Math.toRadians(lon2)
        val lat2Rad = Math.toRadians(lat2)

        val latDiffAndSin = sin((lat2Rad - lat1Rad) / 2.0)
        val latDiffAndSin2 = latDiffAndSin * latDiffAndSin

        val lonDiffAndSin = sin((lon2Rad - lon1Rad) / 2.0)
        val lonDiffAndSin2 = lonDiffAndSin * lonDiffAndSin
        val sqr = latDiffAndSin2 + cos(lat1Rad) * cos(lat2Rad) * lonDiffAndSin2
        return asin(sqrt(sqr)) * 2.0 * EARTH_RADIUS * KM_MULTIPLIER
    }
}