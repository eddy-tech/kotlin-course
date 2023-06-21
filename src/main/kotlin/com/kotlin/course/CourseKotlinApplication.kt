package com.kotlin.course

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CourseKotlinApplication

fun main(args: Array<String>) {
	runApplication<CourseKotlinApplication>(*args)
}
