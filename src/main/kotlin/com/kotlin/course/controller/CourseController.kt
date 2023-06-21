package com.kotlin.course.controller

import com.kotlin.course.dto.CourseDTO
import com.kotlin.course.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses/")
class CourseController (val courseService: CourseService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody courseDTO: CourseDTO): CourseDTO =  courseService.addCourse(courseDTO)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCourse(): List<CourseDTO> = courseService.getAllCourse()

    @PutMapping("{courseId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateCourse(@RequestBody courseDTO: CourseDTO, @PathVariable(name = "courseId") id: Int): CourseDTO = courseService.updateCourse(courseDTO, id)

    @DeleteMapping("{courseId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteCourse(@PathVariable(name = "courseId") id: Int) {
        courseService.deleteCourse(id)
    }
}