package com.tcs.service.utility

import com.tcs.service.model.*

import com.tcs.service.proxy.DeliverymomentClientService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.time.format.DateTimeFormatter
import kotlin.random.Random


@Component
class RestTemplateClient(private val delclient: DeliverymomentClientService) {

    @Autowired
    lateinit var restTemplate: RestTemplate
    private val basePath = "http://deliverymomentcrud-edppublic-deliverymomentcrud-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/deliveryMoment-Crud-service"
    fun validationforUniqueMoments(params: DeliveryMomentModel): Boolean {

        println("result of unique thing")
        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()

        mapParams.put("storeNumber", params.storeNumber.toString());
        mapParams.put("streamNumber", params.streamNumber.toString());
        mapParams.put("deliveryDateTime", params.deliveryDateTime.toString());
        mapParams.put("orderDateTime", params.orderDateTime.toString());
        mapParams.put("fillDateTime", params.fillDateTime.toString());
        var uniqueCheckList :List<DeliveryMomentModel>? =  Utility.convert("$basePath/deliverymomentunique", DeliveryMomentModel(), mapParams)
         println(uniqueCheckList.isNullOrEmpty())
        return uniqueCheckList.isNullOrEmpty()



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
            println("result of date check validation")

        } else {
            return false
        }
    }
    fun maindeliveryflagcheck(params: DeliveryMomentModel): Boolean? {
        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
        mapParams.put("storeNumber", params.storeNumber.toString());
        mapParams.put("streamNumber", params.streamNumber.toString());
        mapParams.put("deliveryDateFrom", params.deliveryDateTime.toString().split(" ")[0])
        mapParams.put("mainDeliveryFlag", "J")
        var uniqueCheckList :List<DeliveryMomentModel>? =  Utility.convert("$basePath/model", DeliveryMomentModel(), mapParams)
        println("result of main delivery validation")
         return   uniqueCheckList?.isNullOrEmpty()
    }
//    fun warehousevalidation(params:DeliveryMomentModel): Boolean {
//        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
//        mapParams.put("storeNumber", params.storeNumber.toString());
//        mapParams.put("deliveryStream", params.streamNumber.toString());
//        mapParams.put("startDate", params.deliveryDateTime.toString().split(" ")[0]);
//        var warehouselist: List<LogisticChannel>? = Utility.convertwo("http://getrefdata-edppublic-getrefdata-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/getReference/logisticChannel", LogisticChannel(), mapParams)
//        if (params.storeOrder != null && warehouselist!=null ) {
//            params.storeOrder!!.forEach {
//                // logic for order number
//                it.wareHouseNumber == warehouselist?.get(0)?.warehouseNumber
//            }
//        }
//        return !warehouselist.isNullOrEmpty()
//    }
//    fun deliverervalidation(params:DeliveryMomentModel): DeliveryMomentModel {
//
//        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
//        mapParams.put("storeNumber", params.storeNumber.toString());
//        mapParams.put("deliveryStream", params.streamNumber.toString());
//        mapParams.put("startDate", params.deliveryDateTime.toString().split(" ")[0]);
//        var delivererlist = Utility.convertOne(
//                "http://getrefdata-edppublic-getrefdata-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/getReference/deliveryChannel", DeliveryChannel(), mapParams)
//        if (delivererlist != null) {
//            params.delivererNumber = delivererlist[0].delivererNumber
//
//        }
//
//        return params
//
//    }
//
//    fun deliveryScheduleValidation(params: DeliveryMomentModel): DeliveryMomentModel {
//        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
//        mapParams.put("storeNumber", params.storeNumber.toString())
//        mapParams.put("deliveryStream", params.streamNumber.toString())
//        mapParams.put("startDate", params.deliveryDateTime.toString().split(" ")[0])
//
//        var deliveryScheduleList = Utility.converthree("http://localhost:8093/api/v1/service-template/deliveryscheduleformoment",DeliveryScheduleModel(),mapParams)
//
//        if(deliveryScheduleList != null) {
//            params.deliverySchemaType = deliveryScheduleList[0].deliverySchemaType
//        }
//
//        return params
//
//    }


fun postForm(params: DeliveryMomentModel) : ResponseEntity<ServiceResponse>? {
        println("inside postform")

    println("inside unique")
    println(validationforUniqueMoments(params))
    println("inside order")
    println(checkDateTime(params.orderDateTime.toString(), params.deliveryDateTime.toString()))
    println("inside delivery")
    println(checkDateTime(params.fillDateTime.toString(),params.deliveryDateTime.toString()))
    println("inside startfill")
    println(checkDateTime(params.startFillTime.toString(),params.deliveryDateTime.toString()))
    println("inside order")
    println(checkDateTime(params.orderDateTime.toString(),params.startFillTime.toString()))
    println("inside flag check")
    println(maindeliveryflagcheck(params))



    if(validationforUniqueMoments(params) && checkDateTime(params.orderDateTime.toString(), params.deliveryDateTime.toString())
            && checkDateTime(params.fillDateTime.toString(),params.deliveryDateTime.toString())
            && checkDateTime(params.startFillTime.toString(),params.deliveryDateTime.toString())
            && checkDateTime(params.orderDateTime.toString(),params.startFillTime.toString())
            )  {
        println("VALIDATION 1")

        if(params.mainDeliveryFlag == "J" && maindeliveryflagcheck(params) == false) {
            println("VALIDATION 2")
            return null
        }

        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
        mapParams.put("storeNumber", params.storeNumber.toString());
        mapParams.put("deliveryStream", params.streamNumber.toString());
        mapParams.put("startDate", params.deliveryDateTime.toString().split(" ")[0]);
        var delivererlist = Utility.convertOne(
                "http://getrefdata-edppublic-getrefdata-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/getReferenceData/deliveryChannel", DeliveryChannel(), mapParams)

        var warehouselist: List<LogisticChannel>? = Utility.convertwo(
                "http://getrefdata-edppublic-getrefdata-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/getReferenceData/logisticChannel", LogisticChannel(), mapParams)

        var deliveryScheduleList = Utility.converthree(
                "http://getrefdata-edppublic-getrefdata-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/getReferenceData/deliveryscheduleformoment",DeliveryScheduleModel(),mapParams)
        println("warehouse")
        println(warehouselist)
        println("deliverer")
        println(delivererlist)
        println("deliveryScheduleList")
        println(deliveryScheduleList)
        if(warehouselist!=null && delivererlist != null
                && deliveryScheduleList != null
                 ) {
            println("nullchecklist")
            params.delivererNumber = delivererlist[0].delivererNumber
            params.deliverySchemaType = deliveryScheduleList[0].deliverySchemaType

            params.id = params.storeNumber.toString() + params.deliveryDateTime + params.streamNumber

            ///need to check to generate sequence
            
            params.storeOrder?.add(StoreOrder(orderNumber = Random.nextLong(100000) ,wareHouseNumber = warehouselist?.get(0)?.warehouseNumber))
        } else {
            return null
        }
        val url = "http://deliverymomentcrud-edppublic-deliverymomentcrud-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/deliveryMoment-Crud-service/model"
       val httpHeaders = HttpHeaders()
       httpHeaders.contentType = MediaType.APPLICATION_JSON
       val requestParams = LinkedMultiValueMap<String, String>()
      //params.forEach(requestParams::add)

       val httpEntity = HttpEntity<DeliveryMomentModel>(params, httpHeaders)
//      // return restTemplate.postForObject(url, httpEntity, ResponseEntity<T>::class.java)
//       // var serv: ResponseEntity<ServiceResponse>? = null

        val response: ResponseEntity<ServiceResponse> = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ServiceResponse::class.java)

//     //    serv = restTemplate.postForObject(url, httpEntity, ServiceResponse::class.java)
       return response

    } else {
        return null
    }

}



    fun putForm(params: DeliveryMomentModel) : ResponseEntity<ServiceResponse>? {
        println("inside putform")

        println("inside unique")
        println(validationforUniqueMoments(params))
        println("inside order")
        println(checkDateTime(params.orderDateTime.toString(), params.deliveryDateTime.toString()))
        println("inside delivery")
        println(checkDateTime(params.deliveryDateTime.toString(),params.fillDateTime.toString()))
        println("inside startfill")
        println(checkDateTime(params.startFillTime.toString(),params.deliveryDateTime.toString()))
        println("inside order")
        println(checkDateTime(params.orderDateTime.toString(),params.startFillTime.toString()))
        println("inside flag check")
        println(maindeliveryflagcheck(params))



        if(checkDateTime(params.orderDateTime.toString(), params.deliveryDateTime.toString())
                && checkDateTime(params.deliveryDateTime.toString(),params.fillDateTime.toString())
                && checkDateTime(params.startFillTime.toString(),params.deliveryDateTime.toString())
                && checkDateTime(params.startFillTime.toString(),params.orderDateTime.toString())
        )  {

//            if(params.mainDeliveryFlag == "J" && maindeliveryflagcheck(params) == false) {
//                return null
//            }

            var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()
            mapParams.put("storeNumber", params.storeNumber.toString());
            mapParams.put("deliveryStream", params.streamNumber.toString());
            mapParams.put("startDate", params.deliveryDateTime.toString().split(" ")[0]);
            var delivererlist = Utility.convertOne(
                    "http://getrefdata-edppublic-getrefdata-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/getReferenceData/deliveryChannel", DeliveryChannel(), mapParams)

            var warehouselist: List<LogisticChannel>? = Utility.convertwo(
                    "http://getrefdata-edppublic-getrefdata-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/getReferenceData/logisticChannel", LogisticChannel(), mapParams)

            var deliveryScheduleList = Utility.converthree(
                    "http://getrefdata-edppublic-getrefdata-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/getReferenceData/deliveryscheduleformoment",DeliveryScheduleModel(),mapParams)

            if(warehouselist!=null && delivererlist != null
            //&& deliveryScheduleList != null
            ) {
                println("nullchecklist")
                params.delivererNumber = delivererlist[0].delivererNumber
                // params.deliverySchemaType = deliveryScheduleList[0].deliverySchemaType

                params.id = params.storeNumber.toString() + params.deliveryDateTime + params.streamNumber

                ///need to check to generate sequence
               // params.storeOrder?.add(StoreOrder(orderNumber = 12355,wareHouseNumber = warehouselist?.get(0)?.warehouseNumber))
            } else {
                return null
            }
            val url = "http://deliverymomentcrud-edppublic-deliverymomentcrud-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/deliveryMoment-Crud-service/model"
            val httpHeaders = HttpHeaders()
            httpHeaders.contentType = MediaType.APPLICATION_JSON
            val requestParams = LinkedMultiValueMap<String, String>()
            //params.forEach(requestParams::add)

            val httpEntity = HttpEntity<DeliveryMomentModel>(params, httpHeaders)
//      // return restTemplate.postForObject(url, httpEntity, ResponseEntity<T>::class.java)
//       // var serv: ResponseEntity<ServiceResponse>? = null

            val response: ResponseEntity<ServiceResponse> = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ServiceResponse::class.java)

//     //    serv = restTemplate.postForObject(url, httpEntity, ServiceResponse::class.java)
            return response

        } else {
            return null
        }

    }


    fun delForm(id:String) {
        val url = "http://deliverymomentcrud-edppublic-deliverymomentcrud-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/deliveryMoment-Crud-service/model/{id}"
        var parametermap:MutableMap<String, String> = mutableMapOf<String, String>()

        parametermap.put("id" ,id)
        println("Call delete function")
        restTemplate.delete(url, parametermap);
        println("Call complete")

    }

}






//    fun  postForm(params: DeliveryMomentModel) : ResponseEntity<ServiceResponse>? {
//
//
//        val currentDateTime = LocalDateTime.now()
//
//
//        println(validationforinsert(params))
//        if (params.endDate!! <= currentDateTime.format(DateTimeFormatter.ISO_DATE) || params.endDate <= params.startDate.toString()
//                || !validationforinsert(params)) {
//            return null
//        }
//
//        else {
//
//
//        val url = "http://delschcrud-edppublic-delschcrud-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/deliveryschedule-crud-service/model"
//        val httpHeaders = HttpHeaders()
//        httpHeaders.contentType = MediaType.APPLICATION_JSON
//        val requestParams = LinkedMultiValueMap<String, String>()
//        //params.forEach(requestParams::add)
//        println("inside postForm")
//        println(requestParams)
//        val httpEntity = HttpEntity<DeliveryScheduleModel>(params, httpHeaders)
//      // return restTemplate.postForObject(url, httpEntity, ResponseEntity<T>::class.java)
//       // var serv: ResponseEntity<ServiceResponse>? = null
//        println("before calling post url")
//        println(url)
//        println(httpEntity)
//        val response: ResponseEntity<ServiceResponse> = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ServiceResponse::class.java)
//        println("after calling post url")
//     //    serv = restTemplate.postForObject(url, httpEntity, ServiceResponse::class.java)
//        return response
//        }
//    }
//
//
//
//    ///For Put request
//    fun  updateForm(params: DeliveryScheduleModel) : ResponseEntity<ServiceResponse>? {
//
//
//        val currentDateTime = LocalDateTime.now()
//
//
//        println(validationforinsert(params))
//        if (params.endDate!! <= currentDateTime.format(DateTimeFormatter.ISO_DATE) || params.endDate <= params.startDate.toString()
//                || !validationforupdate(params)) {
//            return null
//        }
//
//        else {
//
//
//            val url = "http://delschcrud-edppublic-delschcrud-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/deliveryschedule-crud-service/model"
//            val httpHeaders = HttpHeaders()
//            httpHeaders.contentType = MediaType.APPLICATION_JSON
//            val requestParams = LinkedMultiValueMap<String, String>()
//            //params.forEach(requestParams::add)
//            println("inside postForm")
//            println(requestParams)
//            val httpEntity = HttpEntity<DeliveryScheduleModel>(params, httpHeaders)
//            // return restTemplate.postForObject(url, httpEntity, ResponseEntity<T>::class.java)
//            // var serv: ResponseEntity<ServiceResponse>? = null
//          //  println("before calling post url")
//            println(url)
//            println(httpEntity)
//            val response: ResponseEntity<ServiceResponse> = restTemplate.exchange(url, HttpMethod.POST, httpEntity, ServiceResponse::class.java)
//           // println("after calling post url")
//            //    serv = restTemplate.postForObject(url, httpEntity, ServiceResponse::class.java)
//            return response
//        }
//    }
//
//
//    //Validation for put request
//
//    fun validationforupdate(params:DeliveryScheduleModel): Boolean {
//
//        var timetablepassedcheckflagcount : Int =0
//        params.id = params.storeNumber.toString() + params.startDate + params.deliveryStreamNumber
//        //  println(params.deliveryStreamNumber)
//        var basepathforgetallservice = "http://getrefdata-edppublic-getrefdata-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/getReference/"
//        var parametermap:MutableMap<String, String> = mutableMapOf<String, String>()
//        //   println("params is" + params)
//        //   println(basepathforgetallservice+"deliveryStream/"+params.deliveryStreamNumber)
//        var checkdelstream:List<DeliveryStream>? = Utility.
//        convertOne(basepathforgetallservice+"deliveryStream/"+params.deliveryStreamNumber, DeliveryStream(),parametermap)
////                as List<DeliveryStream
//        println(checkdelstream)
//        parametermap.put("storeNumber",params.storeNumber.toString())
//        parametermap.put("deliveryStream",params.deliveryStreamNumber.toString())
//        parametermap.put("startDate",params.startDate.toString())
//        parametermap.put("endDate", params.endDate.toString())
//        var checkdelscheduleoverlap:List<DeliveryScheduleModel>? = Utility.
//        convert(basepathforgetallservice+"deliveryschedulesorted", DeliveryScheduleModel(),parametermap)
//
//        println(checkdelscheduleoverlap)
//
//        // timetable validation
////        var delexample= delclient.getdeliveryscheduleall(params.storeNumber,
////                params.deliveryStreamNumber,params.deliveryStreamName,
////                null, params.startDate,
////                params.endDate,null)?.get(0)
//        //  var timetalevalid : List<Timetable>?  = delexample?.timeTableList
//        var timetablepassed=params.timeTableList
//        var timetablepassedvalidation:MutableList<Timetable>?= mutableListOf()
//        var matchesflag: Boolean
//        println(timetablepassed)
//        if (timetablepassed != null) {
//            timetablepassed.forEach {
//
//
//                for (i in 0..timetablepassed.size-1){
//
//                    var count : Int =0
//                    //     println("it " + it)
//                    //    println(timetablepassed.get(i))
//
//                    if((it .deliveryDay==timetablepassed.get(i).deliveryDay&&it.deliveryTime==timetablepassed.get(i).deliveryTime)||
//                            (it.orderDay==timetablepassed.get(i).orderDay&& it.orderTime==timetablepassed.get(i).orderTime)||
//                            (it.fillDay==timetablepassed.get(i).fillDay&&it.fillTime==timetablepassed.get(i).fillTime))
//                    {
//                        //   println("inside timetabe for loop")
//                        count++
//                        //    println("after count " + count)
//
//                    }
//
//                    //   println("count in if loop outside " + count)
//                    if(count == 1) {
//
//
//                        timetablepassedcheckflagcount ++
//
//                        // println("adding " + timetablepassedcheckflagcount)
//                    }
//                    else {
//                        matchesflag = true
//                    }
//
//                }
//
//
//            }
//        }
//
//        return checkdelstream != null  &&  timetablepassedcheckflagcount == timetablepassed?.size
//    }
//
//    fun deleteById(id:String) {
//        val url = "http://delschcrud-edppublic-delschcrud-dev.59ae6b648ca3437aae3a.westeurope.aksapp.io/api/v1/deliveryschedule-crud-service/model/{id}"
//
//        var parametermap:MutableMap<String, String> = mutableMapOf<String, String>()
//
//        parametermap.put("id" ,id)
//        restTemplate.delete(url, parametermap);
//    }
//

