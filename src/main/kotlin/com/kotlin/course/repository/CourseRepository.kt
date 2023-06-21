package com.kotlin.course.repository;

import com.kotlin.course.entity.Course
import org.springframework.data.jpa.repository.JpaRepository

interface CourseRepository : JpaRepository<Course, Int> {
}