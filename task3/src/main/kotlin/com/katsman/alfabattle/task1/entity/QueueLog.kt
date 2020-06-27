package com.katsman.alfabattle.task1.entity

import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "queue_log")
data class QueueLog(

    @Id
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "data")
    var data: LocalDate? = null,

    @Column(name = "start_time_of_wait")
    var startTimeOfWait: LocalTime? = null,

    @Column(name = "end_time_of_wait")
    var endTimeOfWait: LocalTime? = null,

    @Column(name = "end_time_of_service")
    var endTimeOfService: LocalTime? = null,

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "branches_id")
    var branch: Branch? = null
)