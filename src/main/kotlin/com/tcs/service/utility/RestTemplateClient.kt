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
import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.DaprHttp
import io.dapr.client.domain.HttpExtension
import org.json.JSONArray
import org.springframework.http.*
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random


@Component
class RestTemplateClient {

    val client : DaprClient = DaprClientBuilder().build()
    val mapPair = mapOf(Pair("Content-Type", "application/json"))

    fun validationForUniqueMoments(params: DeliveryMomentModel): Boolean {


        val mapParams: MutableMap<String, String> = mutableMapOf()

        mapParams.putAll(setOf("storeNumber" to params.storeNumber.toString(),"streamNumber" to params.streamNumber.toString(),
                "deliveryDateTime" to params.deliveryDateTime.toString(), "orderDateTime" to params.orderDateTime.toString(),
                "fillDateTime" to params.fillDateTime.toString() ))

        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, mapParams)
        val uniqueCheckList = client.invokeService(SERVICE_APP_ID, "$DEL_MOMENT_CRUD/deliverymomentunique", params,
                httpExtension, mapPair, Array<DeliveryMomentModel>::class.java).block()


        // checking if isdeleted flag is true
        return if(!uniqueCheckList.isNullOrEmpty() && uniqueCheckList[0].isdeleted == true) {

            true

        } else {
            uniqueCheckList.isNullOrEmpty()
        }



    }
    fun checkDateTime(date1:String,date2:String) : Boolean {

        val dateList1 = date1.split(" ")
        val dateList2 = date2.split(" ")

        val dateComp1 = dateList1[0].format(DateTimeFormatter.ISO_DATE)
        val dateComp2 = dateList2[0].format(DateTimeFormatter.ISO_DATE)

    return    when {
            dateComp1 < dateComp2 -> true
            dateComp1 == dateComp2 -> {
                val timeComp1 = dateList1[1].format(DateTimeFormatter.ISO_TIME)
                val timeComp2 = dateList2[1].format(DateTimeFormatter.ISO_TIME)

                (timeComp1 <= timeComp2)
            }

            else -> {
                println("so it basically did it")
              false
            }

        }
    }

    fun mainDeliveryFlagChecks(params: DeliveryMomentModel): Boolean? {
        val mapParams: MutableMap<String, String> = mutableMapOf()
        mapParams.putAll(setOf("storeNumber" to params.storeNumber.toString(),"streamNumber" to params.streamNumber.toString(),"deliveryDateFrom" to params.deliveryDateTime.toString().split(" ")[0],
                "deliveryDateTo" to LocalDate.parse(params.deliveryDateTime.toString().split(" ")[0], DateTimeFormatter.ISO_DATE).plusDays(1).toString(),"mainDeliveryFlag" to "J"))

        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, mapParams)
        val uniqueCheckList = client.invokeService(SERVICE_APP_ID, DEL_MOMENT_CRUD + GET_ALL_URI, params,
                httpExtension, mapPair, Array<DeliveryMomentModel>::class.java).block()

        return   uniqueCheckList.isNullOrEmpty()
    }



fun postForm(params: DeliveryMomentModel) : ResponseEntity<ServiceResponse>? {


    if( validationForUniqueMoments(params) && checkDateTime(params.orderDateTime.toString(), params.deliveryDateTime.toString())
            && checkDateTime(params.deliveryDateTime.toString(),params.fillDateTime.toString())
            && checkDateTime(params.startFillTime.toString(),params.deliveryDateTime.toString())
            && checkDateTime(params.orderDateTime.toString(),params.startFillTime.toString())
            )  {


        if(params.mainDeliveryFlag == "J" && mainDeliveryFlagChecks(params) == false) {

            return null
        }

        val mapParams: MutableMap<String, String> = mutableMapOf()

        mapParams.putAll(setOf("storeNumber" to params.storeNumber.toString(),"deliveryStream" to params.streamNumber.toString(),
                "startDate" to params.deliveryDateTime.toString().split(" ")[0]))

        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, mapParams)

        val delivererList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + DEL_CHANNEL,
                httpExtension, mapPair, Array<DeliveryChannel>::class.java).block()?.toMutableList()

        val warehouseList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + LOG_CHANNEL,
                httpExtension, mapPair, Array<LogisticChannel>::class.java).block()?.toMutableList()

        val deliveryScheduleList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + DEL_SCHEDULE,
                httpExtension, mapPair, Array<DeliveryScheduleModel>::class.java).block()?.toMutableList()



        if(!warehouseList.isNullOrEmpty() && !delivererList.isNullOrEmpty()
                && !deliveryScheduleList.isNullOrEmpty()
                 ) {

            
            params.delivererNumber = delivererList[0].delivererNumber

            params.deliverySchemaType = deliveryScheduleList[0].deliverySchemaType
            
            params.id = params.storeNumber.toString() + params.deliveryDateTime + params.streamNumber

            ///need to check to generate sequence
            
            params.storeOrder?.add(StoreOrder(orderNumber = Random.nextLong(100000) ,warehouseNumber = warehouseList[0].warehouseNumber))
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



    fun putForm(params: DeliveryMomentModel) : ResponseEntity<ServiceResponse>? {

        if(checkDateTime(params.orderDateTime.toString(), params.deliveryDateTime.toString())
                && checkDateTime(params.deliveryDateTime.toString(),params.fillDateTime.toString())
                && checkDateTime(params.startFillTime.toString(),params.deliveryDateTime.toString())
                && checkDateTime(params.orderDateTime.toString(),params.startFillTime.toString())
        ) {

            val mapParams: MutableMap<String, String> = mutableMapOf()

            mapParams.putAll(setOf("storeNumber" to params.storeNumber.toString(),"deliveryStream" to params.streamNumber.toString(),
                    "startDate" to params.deliveryDateTime.toString().split(" ")[0]))

            val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, mapParams)

            val delivererList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + DEL_CHANNEL,
                    httpExtension, mapPair, Array<DeliveryChannel>::class.java).block()?.toMutableList()

            val warehouseList = client.invokeService(SERVICE_APP_ID1, GET_REF_DATA + LOG_CHANNEL,
                    httpExtension, mapPair, Array<LogisticChannel>::class.java).block()?.toMutableList()

            if(!warehouseList.isNullOrEmpty() && !delivererList.isNullOrEmpty()
            ) {

                params.delivererNumber = delivererList[0].delivererNumber

                params.id = params.storeNumber.toString() + params.deliveryDateTime + params.streamNumber



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

        val parametermap:MutableMap<String, String> = mutableMapOf()

        parametermap["id"] = id

        client.invokeService(SERVICE_APP_ID, "$DEL_MOMENT_CRUD$GET_ALL_URI/$id",
                HttpExtension.DELETE, mapPair, JSONArray::class.java).block()

        println("Call complete")

    }

}

