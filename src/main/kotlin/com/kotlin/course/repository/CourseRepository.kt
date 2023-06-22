package com.kotlin.course.repository;

import com.kotlin.course.entity.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import sun.jvm.hotspot.oops.CellTypeState.value

interface CourseRepository : JpaRepository<Course, Int> {
    fun findByNameContaining(name: String): List<Course>

    @Query(value="SELECT * FROM Course WHERE name LIKE %?1%", nativeQuery = true)
    fun findCoursesByName(name: String): List<Course>
}