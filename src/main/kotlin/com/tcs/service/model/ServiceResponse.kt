package com.tcs.service.model

/**
 * This class is for forming the final Response
 */
data class ServiceResponse(
        var responseCode: String = "200",
        var responseDescription: String = "SUCCESS",
        var response: Any? = null
)