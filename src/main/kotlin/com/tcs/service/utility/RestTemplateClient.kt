package com.tcs.service.utility


import com.tcs.service.constant.URLPath.DEL_CHANNEL
import com.tcs.service.constant.URLPath.DEL_MOMENT_CRUD
import com.tcs.service.constant.URLPath.DEL_SCHEDULE
import com.tcs.service.constant.URLPath.GET_ALL_URI
import com.tcs.service.constant.URLPath.GET_REF_DATA
import com.tcs.service.constant.URLPath.LOG_CHANNEL
import com.tcs.service.constant.URLPath.SERVICE_APP_ID
import com.tcs.service.constant.URLPath.SERVICE_APP_ID1
import com.tcs.service.model.*
import com.tcs.service.proxy.DeliverymomentClientService
import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.DaprHttp
import io.dapr.client.domain.HttpExtension
import org.json.JSONArray
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random


@Component
class RestTemplateClient(private val delclient: DeliverymomentClientService, private var restTemplate: RestTemplate) {

    val client : DaprClient = DaprClientBuilder().build()
    val mapPair = mapOf(Pair("Content-Type", "application/json"))

    fun validationForUniqueMoments(params: DeliveryMomentModel): Boolean {

        println("result of unique thing")
        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()

        mapParams.put("storeNumber", params.storeNumber.toString())
        mapParams.put("streamNumber", params.streamNumber.toString())
        mapParams.put("deliveryDateTime", params.deliveryDateTime.toString())
        mapParams.put("orderDateTime", params.orderDateTime.toString())
        mapParams.put("fillDateTime", params.fillDateTime.toString())

        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, mapParams)
        var uniqueCheckList = client.invokeService(SERVICE_APP_ID, DEL_MOMENT_CRUD + "/deliverymomentunique", params,
                httpExtension, mapPair, Array<DeliveryMomentModel>::class.java).block()

        println(uniqueCheckList.isNullOrEmpty())

        // checking if isdeleted flag is true
        return if(!uniqueCheckList.isNullOrEmpty() && uniqueCheckList[0].isdeleted == true) {

            true

        } else {
            uniqueCheckList.isNullOrEmpty()
        }



    }
    fun checkDateTime(date1:String,date2:String) : Boolean {

        var dateList1 = date1.split(" ")
        var dateList2 = date2.split(" ")

        var dateComp1 = dateList1[0].format(DateTimeFormatter.ISO_DATE)
        var dateComp2 = dateList2[0].format(DateTimeFormatter.ISO_DATE)

        if(dateComp1 < dateComp2) {
            return true
        } else if(dateComp1 == dateComp2){
            var timeComp1 = dateList1[1].format(DateTimeFormatter.ISO_TIME)
            var timeComp2 = dateList2[1].format(DateTimeFormatter.ISO_TIME)

            return (timeComp1 <= timeComp2)

        } else {
            return false
        }
    }
    fun mainDeliveryFlagChecks(params: DeliveryMomentModel): Boolean? {
        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
        mapParams.put("storeNumber", params.storeNumber.toString());
        mapParams.put("streamNumber", params.streamNumber.toString());
        mapParams.put("deliveryDateFrom", params.deliveryDateTime.toString().split(" ")[0])
        mapParams.put("deliveryDateTo", LocalDate.parse(params.deliveryDateTime.toString().split(" ")[0], DateTimeFormatter.ISO_DATE).plusDays(1).toString())

        mapParams.put("mainDeliveryFlag", "J")

        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, mapParams)
        var uniqueCheckList = client.invokeService(SERVICE_APP_ID, DEL_MOMENT_CRUD + GET_ALL_URI, params,
                httpExtension, mapPair, Array<DeliveryMomentModel>::class.java).block()
        println(uniqueCheckList.isNullOrEmpty())
        println("result of main delivery validation")
        return   uniqueCheckList.isNullOrEmpty()
    }



fun postForm(params: DeliveryMomentModel) : ResponseEntity<ServiceResponse>? {
        println("inside postform")

    println("inside unique")
    println(validationForUniqueMoments(params))
    println("inside order")
    println(checkDateTime(params.orderDateTime.toString(), params.deliveryDateTime.toString()))
    println("inside delivery")
    println(checkDateTime(params.fillDateTime.toString(),params.deliveryDateTime.toString()))
    println("inside startfill")
    println(checkDateTime(params.startFillTime.toString(),params.deliveryDateTime.toString()))
    println("inside order")
    println(checkDateTime(params.orderDateTime.toString(),params.startFillTime.toString()))
    println("inside flag check")
    println(mainDeliveryFlagChecks(params))


    if( validationForUniqueMoments(params) && checkDateTime(params.orderDateTime.toString(), params.deliveryDateTime.toString())
            && checkDateTime(params.deliveryDateTime.toString(),params.fillDateTime.toString())
            && checkDateTime(params.startFillTime.toString(),params.deliveryDateTime.toString())
            && checkDateTime(params.orderDateTime.toString(),params.startFillTime.toString())
            )  {
        println("VALIDATION 1")

        if(params.mainDeliveryFlag == "J" && mainDeliveryFlagChecks(params) == false) {
            println("VALIDATION 2")
            return null
        }

        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
        mapParams.put("storeNumber", params.storeNumber.toString());
        mapParams.put("deliveryStream", params.streamNumber.toString());
        mapParams.put("startDate", params.deliveryDateTime.toString().split(" ")[0]);

        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, mapParams)

        var delivererList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + DEL_CHANNEL,
                httpExtension, mapPair, Array<DeliveryChannel>::class.java).block()?.toMutableList()

        var warehouseList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + LOG_CHANNEL,
                httpExtension, mapPair, Array<LogisticChannel>::class.java).block()?.toMutableList()

        var deliveryScheduleList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + DEL_SCHEDULE,
                httpExtension, mapPair, Array<DeliveryScheduleModel>::class.java).block()?.toMutableList()

        println("warehouse")
        println(warehouseList)
        println("deliverer")
        println(delivererList)
        println("deliveryScheduleList")
        println(deliveryScheduleList)


        if(!warehouseList.isNullOrEmpty() && !delivererList.isNullOrEmpty()
                && !deliveryScheduleList.isNullOrEmpty()
                 ) {
            println("nullchecklist")
            
            params.delivererNumber = delivererList?.get(0)?.delivererNumber

            params.deliverySchemaType = deliveryScheduleList?.get(0)?.deliverySchemaType
            
            params.id = params.storeNumber.toString() + params.deliveryDateTime + params.streamNumber

            ///need to check to generate sequence
            
            params.storeOrder?.add(StoreOrder(orderNumber = Random.nextLong(100000) ,warehouseNumber = warehouseList?.get(0)?.warehouseNumber))
        } else {
            return null
        }

        
        println("it comes to hit crud")
        println(params)
        
        val response = client.invokeService(SERVICE_APP_ID, DEL_MOMENT_CRUD + GET_ALL_URI, params,
                HttpExtension.POST, mapPair, JSONArray::class.java).block()
        
      return ResponseEntity.ok(ServiceResponse("200",
              "Success", response))


    } else {
        return null
    }

}



    fun putForm(params: DeliveryMomentModel) : ResponseEntity<ServiceResponse>? {

        println("inside putform")

        println("inside unique")
        println(validationForUniqueMoments(params))
        println("inside order")
        println(checkDateTime(params.orderDateTime.toString(), params.deliveryDateTime.toString()))
        println("inside delivery")
        println(checkDateTime(params.deliveryDateTime.toString(),params.fillDateTime.toString()))
        println("inside startfill")
        println(checkDateTime(params.startFillTime.toString(),params.deliveryDateTime.toString()))
        println("inside order")
        println(checkDateTime(params.orderDateTime.toString(),params.startFillTime.toString()))
        println("inside flag check")
        println(mainDeliveryFlagChecks(params))



        if(checkDateTime(params.orderDateTime.toString(), params.deliveryDateTime.toString())
                && checkDateTime(params.deliveryDateTime.toString(),params.fillDateTime.toString())
                && checkDateTime(params.startFillTime.toString(),params.deliveryDateTime.toString())
                && checkDateTime(params.orderDateTime.toString(),params.startFillTime.toString())
        ) {

             println("It is trying to update")
            val mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
            mapParams.put("storeNumber", params.storeNumber.toString());
            mapParams.put("deliveryStream", params.streamNumber.toString());
            mapParams.put("startDate", params.deliveryDateTime.toString().split(" ")[0])

            val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, mapParams)

            val delivererList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + DEL_CHANNEL,
                    httpExtension, mapPair, Array<DeliveryChannel>::class.java).block()?.toMutableList()

            val warehouseList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + LOG_CHANNEL,
                    httpExtension, mapPair, Array<LogisticChannel>::class.java).block()?.toMutableList()

            if(!warehouseList.isNullOrEmpty() && !delivererList.isNullOrEmpty()
            ) {
                println("nullchecklist")
                params.delivererNumber = delivererList[0].delivererNumber

                params.id = params.storeNumber.toString() + params.deliveryDateTime + params.streamNumber

                ///need to check to generate sequence

               } else {
                return null
            }

            val response = client.invokeService(SERVICE_APP_ID, DEL_MOMENT_CRUD + GET_ALL_URI, params,
                    HttpExtension.POST, mapPair, JSONArray::class.java).block()


            return ResponseEntity.ok(ServiceResponse("200",
                    "Success", response))


        } else {
            return null
        }

    }


    fun delForm(id:String) {

        val parametermap:MutableMap<String, String> = mutableMapOf<String, String>()

        parametermap.put("id" ,id)

        println(id)
        
        println("Call delete function")
        client.invokeService(SERVICE_APP_ID, DEL_MOMENT_CRUD + GET_ALL_URI + "/$id",
                HttpExtension.DELETE, mapPair, JSONArray::class.java).block()

        println("Call complete")

    }

}

