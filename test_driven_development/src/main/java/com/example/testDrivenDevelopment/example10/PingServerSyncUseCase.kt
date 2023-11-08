package com.example.testDrivenDevelopment.example10

import com.example.testDrivenDevelopment.example10.networking.PingServerHttpEndpointSync

class PingServerSyncUseCase(private val mPingServerHttpEndpointSync: PingServerHttpEndpointSync) {
    enum class UseCaseResult {
        FAILURE, SUCCESS
    }

    fun pingServerSync(): UseCaseResult {
        val result = mPingServerHttpEndpointSync.pingServerSync()
        return when (result) {
            PingServerHttpEndpointSync.EndpointResult.GENERAL_ERROR, PingServerHttpEndpointSync.EndpointResult.NETWORK_ERROR -> UseCaseResult.FAILURE
            PingServerHttpEndpointSync.EndpointResult.SUCCESS -> UseCaseResult.SUCCESS
            else -> throw RuntimeException("invalid result: $result")
        }
    }
}
