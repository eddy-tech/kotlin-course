package com.kotlin.course.entity

import com.kotlin.course.dto.CourseDTO

fun courseEntityList() = listOf(
    Course(null,
    "Build restful APIs using spring boot with kotlin",
    "Development"),
    Course(null,
    "Build reactive microservices using webFlux/springboot",
    "Development"),
    Course(null,
    "How to become Fullstack java for java developers",
    "Development")
  )

fun courseDTO(
    id: Int? = null,
    name: String = "Build restful APIs using spring boot with kotlin",
    category: String = "Dilip Jeans",
) = CourseDTO(
    id,
    name,
    category
)
