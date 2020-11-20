package com.tcs.service.model




data class DeliveryChannel (
        val deliveryStream: Int?=0,
        val storeNumber:Long?=0,
        val startDate: String?="",
        val endDate: String?="",
        val delivererNumber: Int=0
)