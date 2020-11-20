package com.tcs.service.proxy

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.tcs.service.model.DeliveryMomentModel
import com.tcs.service.utility.Utility
import org.apache.logging.log4j.kotlin.logger
import org.json.JSONObject
import org.springframework.stereotype.Service

@Service
class DeliverymomentClientService : DeliveryMoment<DeliveryMomentModel> {
    val logger = logger()
    private val basePath = "http://localhost:8095/api/v1/deliveryMoment-Crud-service"
    override fun getdeliverymomentall(storeNumber: Long?, StreamNumber: Int?,
                                      schemaName: String?,deliveryDateTime:String?,orderDateTime:String?,
                                      fillDateTime:String?,
                                      startFillTime:String?, deliveryDateFrom:String?, deliveryDateTo:String?,
                                      orderDateFrom:String?, orderDateTo:String?, fillDateFrom:String?,
                                      fillDateTo:String?, startFillTimeFrom:String?, startFillTimeTo:String?,
                                      logisticGroupNumber:Int?)
            : List<DeliveryMomentModel>? {
        var mapParams: MutableMap<String, String> = mutableMapOf<String, String>()

        if(storeNumber == null && StreamNumber == null &&
                schemaName == null && deliveryDateTime == null
                && orderDateTime == null && fillDateTime == null
                && logisticGroupNumber== null
                && startFillTime == null){
            return Utility.convert("$basePath/model", DeliveryMomentModel(), mapParams)
        }

        if( storeNumber != null) {
//
            mapParams.put("storeNumber", storeNumber.toString());
        }

        if( StreamNumber != null) {
            mapParams.put("streamNumber", StreamNumber.toString());
        }


        if( schemaName != null) {
            mapParams.put("schemaName", schemaName);
        }
        if( deliveryDateTime != null) {
            mapParams.put("deliveryDateTime", deliveryDateTime);
        }

        if( orderDateTime != null) {
            mapParams.put("orderDateTime", orderDateTime);
        }

        if( fillDateTime != null) {
            mapParams.put("fillDateTime", fillDateTime);
        }

        if( startFillTime != null) {
                mapParams.put("startFillTime", startFillTime);
        }
        if(logisticGroupNumber != null){
            mapParams.put("logisticGroupNumber", logisticGroupNumber.toString());
        }


        return Utility.convert("$basePath/model", DeliveryMomentModel(), mapParams)
    }
    companion object {

        fun convertList(jsonObject: JSONObject): List<DeliveryMomentModel> {
            return when {
                jsonObject.has("response") -> {
                    val mapper = ObjectMapper().registerKotlinModule()
                    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                    mapper.readValue<List<DeliveryMomentModel>>(jsonObject["response"].toString(),
                            object : TypeReference<List<DeliveryMomentModel>>() {})
                }
                else -> {
                    listOf()
                }
            }
        }
    }

}