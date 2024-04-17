package com.example.testDrivenDevelopment.exercise7.networking

interface GetReputationHttpEndpointSync {
    enum class EndpointStatus {
        SUCCESS,
        GENERAL_ERROR,
        NETWORK_ERROR,
    }

    class EndpointResult(val status: EndpointStatus, val reputation: Int)

    val reputationSync: EndpointResult?
}
