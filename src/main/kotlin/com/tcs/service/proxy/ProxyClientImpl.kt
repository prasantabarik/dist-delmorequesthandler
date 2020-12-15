package com.tcs.service.proxy


import com.tcs.service.constant.URLPath
import com.tcs.service.constant.URLPath.DEL_MOMENT_CRUD
import com.tcs.service.model.DeliveryMomentModel
import com.tcs.service.utility.Utility
import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.DaprHttp
import io.dapr.client.domain.HttpExtension
import org.springframework.stereotype.Service
import java.util.*



@Service
class DeliverymomentClientService : DeliveryMoment<DeliveryMomentModel> {

    override fun validateForParams(storeNumber: Long?, streamNumber: Int?, schemaName: String?, deliveryDateTime: String?, orderDateTime: String?, fillDateTime: String?, startFillTime: String?, deliveryDateFrom: String?, deliveryDateTo: String?, orderDateFrom: String?, orderDateTo: String?, fillDateFrom: String?, fillDateTo: String?, startFillTimeFrom: String?, startFillTimeTo: String?, logisticGroupNumber: Int?, mainDeliveryFlag: String?): MutableMap<String, String> {
        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()


        if(storeNumber == null && streamNumber == null &&
                schemaName == null && deliveryDateTime == null && deliveryDateFrom == null && deliveryDateTo == null
                && startFillTime == null && startFillTimeFrom == null && startFillTimeTo == null && orderDateTime == null && orderDateFrom == null && orderDateTo == null
                && logisticGroupNumber== null && fillDateTime == null && fillDateFrom == null && fillDateTo == null
                && mainDeliveryFlag == null){
            println("Inside IF")
            return mapParams
        }
        if( storeNumber != null) {
            mapParams.put("storeNumber", storeNumber.toString());
        }

        if( streamNumber != null) {
            mapParams.put("streamNumber", streamNumber.toString());
        }

        if( schemaName != null) {
            mapParams.put("schemaName", schemaName);
        }
        if( deliveryDateTime != null) {
            mapParams.put("deliveryDateTime", deliveryDateTime);
        }
        if( deliveryDateFrom != null) {
            mapParams.put("deliveryDateFrom", deliveryDateFrom);
        }
        if( deliveryDateTo != null) {
            mapParams.put("deliveryDateTo", deliveryDateTo);
        }

        if( orderDateTime != null) {
            mapParams.put("orderDateTime", orderDateTime);
        }
        if( orderDateFrom != null) {
            mapParams.put("orderDateFrom", orderDateFrom);
        }
        if( orderDateTo != null) {
            mapParams.put("orderDateTo", orderDateTo);
        }

        if( fillDateTime != null) {
            mapParams.put("fillDateTime", fillDateTime);
        }
        if( fillDateFrom != null) {
            mapParams.put("fillDateFrom", fillDateFrom);
        }
        if( fillDateTo != null) {
            mapParams.put("fillDateTo", fillDateTo);
        }

        if( startFillTime != null) {
            mapParams.put("startFillTime", startFillTime);
        }
        if( startFillTimeFrom != null) {
            mapParams.put("startFillTimeFrom", startFillTimeFrom);
        }
        if( startFillTimeTo != null) {
            mapParams.put("startFillTimeTo", startFillTimeTo);
        }
        if(logisticGroupNumber != null){
            mapParams.put("logisticGroupNumber", logisticGroupNumber.toString());
        }
        if(mainDeliveryFlag != null){
            mapParams.put("mainDeliveryFlag", mainDeliveryFlag.toString());
        }
        return mapParams
    }


    override fun getDeliveryMomentAll(storeNumber: Long?, streamNumber: Int?,
                                      schemaName: String?,deliveryDateTime:String?,orderDateTime:String?,
                                      fillDateTime:String?,
                                      startFillTime:String?, deliveryDateFrom:String?, deliveryDateTo:String?,
                                      orderDateFrom:String?, orderDateTo:String?, fillDateFrom:String?,
                                      fillDateTo:String?, startFillTimeFrom:String?, startFillTimeTo:String?,
                                      logisticGroupNumber:Int?, mainDeliveryFlag: String?)
            : MutableList<DeliveryMomentModel>? {
        val mapParams =  validateForParams(storeNumber, streamNumber,
                  schemaName,deliveryDateTime, orderDateTime,
                  fillDateTime, startFillTime, deliveryDateFrom,
                  deliveryDateTo, orderDateFrom, orderDateTo,
                  fillDateFrom, fillDateTo,
                  startFillTimeFrom, startFillTimeTo,logisticGroupNumber, mainDeliveryFlag)

        val client : DaprClient = DaprClientBuilder().build()
        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, mapParams)

        val res1 = client.invokeService(URLPath.SERVICE_APP_ID, DEL_MOMENT_CRUD + URLPath.GET_ALL_URI,
                httpExtension, mapOf(Pair("Content-Type", "application/json")), Array<DeliveryMomentModel>::class.java).block()?.toMutableList()

        return res1


    }



}
