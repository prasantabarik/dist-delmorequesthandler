package com.tcs.service.utility

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.tcs.service.constant.URLPath.DEL_MOMENT_CRUD
import com.tcs.service.constant.URLPath.GET_ALL_URI
import com.tcs.service.model.*
import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.DaprHttp
import io.dapr.client.domain.HttpExtension
import khttp.get
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.http.HttpEntity
import reactor.core.publisher.Mono
import java.io.ByteArrayInputStream


object Utility {

    fun invokeFromDapr(params: MutableMap<String, String>, objectType: Any): Any? {

        val SERVICE_APP_ID = "DeliveryMomentCRUD"

        val client : DaprClient = DaprClientBuilder().build()
        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, params)

        println("RES1")
        val res1 = client.invokeService(SERVICE_APP_ID, DEL_MOMENT_CRUD + GET_ALL_URI, params,
                httpExtension, mapOf(Pair("Content-Type", "application/json")), ServiceResponse::class.java).block()


        return  res1?.response
//        val res1 = client.invokeService(SERVICE_APP_ID, "api/v1/deliveryMoment-Crud-service/model", params,
//                httpExtension, mapOf(Pair("Content-Type", "application/json")), JSONArray[("response")]::class.java).block()

//
//        println(params)
//         val res2 = res1 as JSONObject
//
//        println(res1)
//
//        val mapper = ObjectMapper().registerKotlinModule()
//        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
//        println(objectType)
//
//        return mapper.readValue(res1?.get(2)?.toString(), Array<DeliveryMomentModel>::class.java).toMutableList()
//
////        return res1
        }


    fun convert(url: String, objectType: Any, params: MutableMap<String, String>): List<DeliveryMomentModel>? {

        val jsonObject = get(url = url, params = params).jsonObject
        println("JSON Object")
        println(jsonObject)


        return when (true) {
            true -> {
                val mapper = ObjectMapper().registerKotlinModule()
                mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                println(objectType)
                when (objectType) {
                    is DeliveryMomentModel -> {
                        print(mapper)

                        mapper.readValue(jsonObject["response"].toString(), Array<DeliveryMomentModel>::class.java).toMutableList()
                    }
                    else -> {
                        null
                    }
                }
            }
            false -> {
                null
            }
        }
    }

    fun convertOne(url: String, objectType: Any, params: MutableMap<String, String>): List<DeliveryChannel>? {

        val jsonObject = get(url = url, params = params).jsonObject
        println(jsonObject)
        return when (true){
            true -> {
                val mapper = ObjectMapper().registerKotlinModule()
                mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                println(objectType)
                when (objectType) {
                    is DeliveryChannel -> {
                        mapper.readValue(jsonObject["response"].toString(), Array<DeliveryChannel>::class.java).toMutableList()
                    }
                    else -> {
                        null
                    }
                }
            }
            false -> {
                null
            }
        }
    }
    fun convertTwo(url: String, objectType: Any, params: MutableMap<String, String>): List<LogisticChannel>? {

        val jsonObject = get(url = url, params = params).jsonObject
        println(jsonObject)
        return when (true) {
            true -> {
                val mapper = ObjectMapper().registerKotlinModule()
                mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                println(objectType)
                when (objectType) {

                    is LogisticChannel -> {
                        mapper.readValue(jsonObject["response"].toString(), Array<LogisticChannel>::class.java).toMutableList()
                    }
                    else -> {
                        null
                    }
                }
            }
            false -> {
                null
            }
        }
    }

    // for del schedule
    fun convertThree(url: String, objectType: Any, params: MutableMap<String, String>): List<DeliveryScheduleModel>? {

        val jsonObject = get(url = url, params = params).jsonObject
        println(jsonObject)
        return when (true){
            true -> {
                val mapper = ObjectMapper().registerKotlinModule()
                mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                println(objectType)
                when (objectType) {
                    is DeliveryScheduleModel -> {
                        mapper.readValue(jsonObject["response"].toString(), Array<DeliveryScheduleModel>::class.java).toMutableList()
                    }
                    else -> {
                        null
                    }
                }
            }
            false -> {
                null
            }
        }
    }


}
