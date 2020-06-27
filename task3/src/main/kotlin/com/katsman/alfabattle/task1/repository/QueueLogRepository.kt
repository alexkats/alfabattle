package com.katsman.alfabattle.task1.repository

import com.katsman.alfabattle.task1.entity.Branch
import com.katsman.alfabattle.task1.entity.QueueLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QueueLogRepository : JpaRepository<QueueLog, Long> {

    fun findAllByBranch(branch: Branch): Set<QueueLog>
}