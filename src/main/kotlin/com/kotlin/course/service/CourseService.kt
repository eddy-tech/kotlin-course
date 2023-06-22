package com.kotlin.course.service

import com.kotlin.course.dto.CourseDTO
import com.kotlin.course.entity.Course

interface CourseService {

    fun addCourse(courseDTO: CourseDTO): Any
    fun getAllCourse(): List<CourseDTO>
    fun updateCourse(courseDTO: CourseDTO, id: Int): CourseDTO
    fun deleteCourse(id: Int)
}