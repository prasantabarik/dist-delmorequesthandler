package com.tcs.service.utility

import com.tcs.service.constant.URLPath.DEL_MOMENT_CRUD
import com.tcs.service.constant.URLPath.GET_ALL_URI
import com.tcs.service.constant.URLPath.SERVICE_APP_ID
import com.tcs.service.model.*
import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.DaprHttp
import io.dapr.client.domain.HttpExtension


object Utility {

    fun invokeFromDapr(params: MutableMap<String, String>): MutableList<DeliveryMomentModel>? {


        val client : DaprClient = DaprClientBuilder().build()
        val httpExtension = HttpExtension(DaprHttp.HttpMethods.GET, params)

        val res1 = client.invokeService(SERVICE_APP_ID, DEL_MOMENT_CRUD + GET_ALL_URI,
                httpExtension, mapOf(Pair("Content-Type", "application/json")), Array<DeliveryMomentModel>::class.java).block()?.toMutableList()

        return res1

        }


}
