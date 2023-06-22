package com.kotlin.course.contrroller

import com.kotlin.course.dto.CourseDTO
import com.kotlin.course.entity.Course
import com.kotlin.course.entity.courseEntityList
import com.kotlin.course.repository.CourseRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
 class CourseControllerIntTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setup(){
        courseRepository.deleteAll()
        val courses = courseEntityList()
        courseRepository.saveAll(courses)
    }

    @Test
    fun givenCourseObject_whenSaveCourse_thenReturnCourseObject() {
        val courseDTO = CourseDTO(null, "Build restful APIs using spring boot and kotlin", "Amine Mohamed")

        val saveCourseDTO = webTestClient
            .post()
            .uri("/v1/courses/")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertTrue(saveCourseDTO!!.id != null)
    }

    @Test
    fun givenListCourse_whenGetAllListOfCourseObjects_thenReturnListOfCourse(){
        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses/")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courseDto : $courseDTOs")
        assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun givenCourseObjectAndCourseId_whenUpdateCourseObject_thenReturnCourseObject(){
        // existing course
        val course = Course(null, "Build restful APIs using spring boot and kotlin", "Development")
        courseRepository.save(course)

        //courseId - Updated courseDTOs
        val updateCourseDTO = CourseDTO(null, "Build restful APIs using spring boot and kotlin-java", "Development")

        val updateCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", course.id)
            .bodyValue(updateCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals("Build restful APIs using spring boot and kotlin-java", updateCourse?.name)

    }

    @Test
    fun givenCourseId_whenDeleteCourse_thenReturnNothing(){
        val course = Course(null, "Build restful APIs using spring boot and kotlin", "Development")
        courseRepository.save(course)

        val deleteCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent
    }
}