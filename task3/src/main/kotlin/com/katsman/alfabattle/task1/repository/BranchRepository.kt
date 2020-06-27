package com.katsman.alfabattle.task1.repository

import com.katsman.alfabattle.task1.entity.Branch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BranchRepository : JpaRepository<Branch, Long>