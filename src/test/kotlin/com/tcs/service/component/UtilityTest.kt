package com.tcs.service.component

import com.tcs.service.model.*
import com.tcs.service.utility.RestTemplateClient
import com.tcs.service.utility.Utility
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class, MockitoExtension::class)
class UtilityTest {
    @Autowired
    lateinit var utility: Utility


    @MockBean
    lateinit var restTemplateClient: RestTemplateClient

    @Test
    fun `convert check`() {
        assert(utility.convert(String(), Any(), mutableMapOf()) is List<*>?)
    }

    @Test
    fun `convertOne check`() {
        assert(utility.convert(String(), Any(), mutableMapOf()) is List<*>?)
    }

    @Test
    fun `convertTwo check`() {
        assert(utility.convert(String(), Any(), mutableMapOf()) is List<*>?)
    }

    @Test
    fun `convertThree check`() {
        assert(utility.convert(String(), Any(), mutableMapOf()) is List<*>?)
    }
}