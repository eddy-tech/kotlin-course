package com.kotlin.course.service

import com.kotlin.course.dto.CourseDTO
import com.kotlin.course.entity.Course
import com.kotlin.course.exceptions.CourseNotFoundException
import org.springframework.stereotype.Service
import com.kotlin.course.repository.CourseRepository
import lombok.AllArgsConstructor
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired


@Service
@AllArgsConstructor
class CourseServiceImpl @Autowired constructor(private val courseRepository: CourseRepository) : CourseService {
    companion object : KLogging()

    override fun addCourse(courseDTO: CourseDTO): CourseDTO {
        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category)
        }
        courseRepository.save(courseEntity)

        logger.info("saved course is $courseEntity")

        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category)
        }
    }

    override fun getAllCourse(): List<CourseDTO> {
        val mutableList = courseRepository.findAll()
            .map {
            CourseDTO(it.id, it.name, it.category)
        }
            .toList()

        return mutableList
    }

    override fun updateCourse(courseDTO: CourseDTO, id: Int): CourseDTO {
        val existingCourse = courseRepository.findById(id)
        val updateCourse: CourseDTO?
        if(existingCourse.isPresent) {
             updateCourse = existingCourse.get()
                .let {
                    it.name = courseDTO.name
                    it.category = courseDTO.category
                    courseRepository.save(it)
                    CourseDTO(it.id, it.name, it.category)
                }
        } else {
            logger.error { "This course has null id" }
            throw CourseNotFoundException("This course with id = $id has not been found")
        }

        return updateCourse
    }

    override fun deleteCourse(id: Int) {
        val existingCourse = courseRepository.findById(id)

        if(existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    courseRepository.deleteById(id)
                }
        } else {
            logger.error { "This course has null id" }
            throw CourseNotFoundException("This course with id = $id has not been found")
        }
    }

}