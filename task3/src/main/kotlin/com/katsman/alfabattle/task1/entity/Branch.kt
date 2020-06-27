package com.katsman.alfabattle.task1.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "branches")
data class Branch(

    @Id
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "title")
    var title: String? = null,

    @Column(name = "lon")
    var lon: String? = null,

    @Column(name = "lat")
    var lat: String? = null,

    @Column(name = "address")
    var address: String? = null
)