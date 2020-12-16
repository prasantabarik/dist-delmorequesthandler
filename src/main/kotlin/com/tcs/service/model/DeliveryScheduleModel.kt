package com.tcs.service.model





data class DeliveryScheduleModel(
        var id: String? = "",
        var storeNumber: Long?=0,
        var deliveryStreamNumber: Int?=0,
        var deliveryStreamName: String?="",
        var schemaName: String?="",
        var deliverySchemaType: Int?=0,
        var startDate: String?="",
        var endDate: String?="",
        var notes: String?=""



)