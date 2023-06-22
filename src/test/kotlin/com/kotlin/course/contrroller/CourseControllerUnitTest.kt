package com.kotlin.course.contrroller

import com.kotlin.course.controller.CourseController
import com.kotlin.course.dto.CourseDTO
import com.kotlin.course.entity.Course
import com.kotlin.course.entity.courseDTO
import com.kotlin.course.entity.courseEntityList
import com.kotlin.course.repository.CourseRepository
import io.mockk.every
import com.kotlin.course.service.CourseServiceImpl
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient : WebTestClient

    @MockBean
    lateinit var courseServiceMockK : CourseServiceImpl

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
        every { courseServiceMockK.addCourse(any())} returns courseDTO(id = 1)

        val saveCourseDTO = webTestClient
            .post()
            .uri("/v1/courses/")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue(saveCourseDTO!!.id != null)
    }

    @Test
    fun givenEmptyCourseObject_whenSaveCourse_thenThrowExceptionValidation() {
        val courseDTO = CourseDTO(null, "", "")
        every { courseServiceMockK.addCourse(any())} returns courseDTO(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/courses/")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("courseDTO.category must not be empty, courseDTO.name must not be empty", response)
    }


    @Test
    fun givenCourseObject_whenSaveCourse_thenThrowRuntimeException() {
        val courseDTO = CourseDTO(null, "Build restful APIs using spring boot and kotlin", "Amine Mohamed")
        val errorMessage = "Unexpected Error occurred"
        every { courseServiceMockK.addCourse(any())} throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses/")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMessage, response)
    }

    @Test
    @Disabled
    fun givenListCourse_whenGetAllListOfCourseObjects_thenReturnListOfCourse(){
        every { courseServiceMockK.getAllCourse()}.returnsMany (
                listOf(courseDTO(id = 1),
                    courseDTO(id = 2,
                        name = "Build reactive microservices using webFlux/springboot")
                    )
                )

        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses/")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("courseDto : $courseDTOs")
        Assertions.assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun givenCourseObjectAndCourseId_whenUpdateCourseObject_thenReturnCourseObject(){
        // existing course
        val course = Course(null, "Build restful APIs using spring boot and kotlin", "Development")
        every { courseServiceMockK.updateCourse(any(), any()) } returns courseDTO(id = 100, name = "Build restful APIs using spring boot and kotlin-java")
        //courseId - Updated courseDTOs
        val updateCourseDTO = CourseDTO(null, "Build restful APIs using spring boot and kotlin-java", "Development")

        val updateCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}",100)
            .bodyValue(updateCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build restful APIs using spring boot and kotlin-java", updateCourse!!.name)

    }

    @Test
    fun givenCourseId_whenDeleteCourse_thenReturnNothing(){
        every{ courseServiceMockK.deleteCourse(any())} just runs

        val deleteCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent
    }
}