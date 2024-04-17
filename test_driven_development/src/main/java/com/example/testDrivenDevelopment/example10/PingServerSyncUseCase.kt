package com.example.testDrivenDevelopment.example10

import com.example.testDrivenDevelopment.example10.networking.PingServerHttpEndpointSync
import com.example.testDrivenDevelopment.example10.networking.PingServerHttpEndpointSync.EndpointResult.GENERAL_ERROR
import com.example.testDrivenDevelopment.example10.networking.PingServerHttpEndpointSync.EndpointResult.NETWORK_ERROR
import com.example.testDrivenDevelopment.example10.networking.PingServerHttpEndpointSync.EndpointResult.SUCCESS

class PingServerSyncUseCase(private val pingServerHttpEndpointSync: PingServerHttpEndpointSync) {
    enum class UseCaseResult {
        FAILURE,
        SUCCESS,
    }

    fun pingServerSync(): UseCaseResult {
        return when (val result = pingServerHttpEndpointSync.pingServerSync()) {
            GENERAL_ERROR, NETWORK_ERROR -> UseCaseResult.FAILURE
            SUCCESS -> UseCaseResult.SUCCESS
            else -> throw RuntimeException("invalid result: $result")
        }
    }
}
