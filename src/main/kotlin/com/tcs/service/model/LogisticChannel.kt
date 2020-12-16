package com.tcs.service.model

data class LogisticChannel(
        val logisticGroupNumber: Int?=0,
        val storeNumber: Long?=0,
        val startDate: String?="",
        val deliveryStream: Int?=0,
        val warehouseNumber: Int?=0,
        val endDate: String?=""
)
