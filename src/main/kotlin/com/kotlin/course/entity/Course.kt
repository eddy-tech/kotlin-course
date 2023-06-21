package com.kotlin.course.entity

import jakarta.persistence.*
import mu.KLogging

@Entity
@Table(name = "Courses")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ids", nullable = false)
    val id : Int?,
    @Column(name = "noms", nullable = false)
    val name : String,
    @Column(name = "categories", nullable = false)
    val category : String
)