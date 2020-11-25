package com.tcs.service.component

import com.tcs.service.model.LogisticGroupExclusion
import org.springframework.beans.factory.annotation.Value
import com.tcs.service.model.DeliveryMomentModel

open class BaseTest {

    @Value("\${service.test-data.id}")
    public val dataId: String = "0"
    public val storeNumber: Long?=0
    public val streamNumber: Int? =0

    public val schemaName: String? =""
    public val deliveryDateTime: String?="2020-11-24"
    public val orderDateTime: String? ="2020-11-20"

    public val fillDateTime: String?="2020-11-24"
    public val mainDeliveryFlag: String?="J"

    public val startFillTime: String?="2020-11-24"

    public val deliveryDateFrom:String? = "abc"
    public val deliveryDateTo:String? = "xyz"
    public val orderDateFrom:String? = "abc"
    public val orderDateTo:String? = "xyz"
    public val fillDateFrom:String? = "abc"
    public val fillDateTo:String? = "xyz"
    public val startFillTimeFrom:String? = "abc"
    public val startFillTimeTo:String? = "xyz"
    public val logisticGroupNumber:Int? = 0
    public val model: DeliveryMomentModel = DeliveryMomentModel()
    public val Baseurl: String = ""
    public val objectType: Any = DeliveryMomentModel()
    public val params: MutableMap<String, String> = mutableMapOf()
}