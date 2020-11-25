package com.tcs.service.component

import com.nhaarman.mockito_kotlin.whenever
import com.tcs.service.constant.URLPath
import com.tcs.service.model.DeliveryMomentModel
import com.tcs.service.model.ServiceResponse
import com.tcs.service.proxy.DeliveryMoment
import com.tcs.service.proxy.DeliverymomentClientService
import com.tcs.service.service.Service
import com.tcs.service.utility.RestTemplateClient
import com.tcs.service.utility.Utility
import com.tcs.service.utility.getModel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.get
import java.io.File

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class, MockitoExtension::class)
class RestTemplateTest {


    @Autowired
    lateinit var restTemplateClient: RestTemplateClient

    @MockBean
    lateinit var utility: Utility




    /**
     * Preparing Mock Stub For service class
     **/
    @BeforeEach
    fun setup() {
        whenever( utility.convert(url = "", objectType = DeliveryMomentModel(), params = mutableMapOf())).thenAnswer { getModel() }
        whenever( utility.convertOne(url = "", objectType = DeliveryMomentModel(), params = mutableMapOf())).thenAnswer { getModel() }
        whenever( utility.convertwo(url = "", objectType = DeliveryMomentModel(), params = mutableMapOf())).thenAnswer { getModel() }
        whenever( utility.converthree(url = "", objectType = DeliveryMomentModel(), params = mutableMapOf())).thenAnswer { getModel() }

//        whenever(postService.postForm(model)).thenAnswer { getModel() }
//        whenever(service.getById(id = dataId)).thenAnswer { getModel() }
//        whenever(service.getById(id = dataId)).thenAnswer { getModel() }

    }

    /**
     * Test Method  for Controller Get Endpoint
     * Service call is mocked
     **/
    @Test
    fun `postForm check`() {
        assert(restTemplateClient.postForm(DeliveryMomentModel()) is ResponseEntity<ServiceResponse>)
    }

    @Test
    fun `putForm Check`() {
        assert(restTemplateClient.postForm(DeliveryMomentModel()) is ResponseEntity<ServiceResponse>)
    }

    @Test
    fun `validationForUniqueMoments`() {
        assert(restTemplateClient.validationforUniqueMoments(DeliveryMomentModel()) is Boolean)
    }

    @Test
    fun `checkDateTime`() {
        assert(restTemplateClient.checkDateTime(String(), String()) is Boolean)
    }

    @Test
    fun `mainDeliveryFlagCheck`() {
        assert(restTemplateClient.maindeliveryflagcheck(DeliveryMomentModel()) is Boolean)
    }

    @Test
    fun `delete`(){
        assert(restTemplateClient.delForm(String()) is Unit)
    }
}