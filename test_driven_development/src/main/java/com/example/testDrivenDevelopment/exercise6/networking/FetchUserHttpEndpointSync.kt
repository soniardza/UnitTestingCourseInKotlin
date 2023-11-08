package com.example.testDrivenDevelopment.exercise6.networking

interface FetchUserHttpEndpointSync {
    enum class EndpointStatus {
        SUCCESS, AUTH_ERROR, GENERAL_ERROR
    }

    class EndpointResult(val status: EndpointStatus, val userId: String, val username: String)

    /**
     * Get user details
     * @return the aggregated result
     * @throws NetworkErrorException if operation failed due to network error
     */
    @Throws(NetworkErrorException::class)
    fun fetchUserSync(userId: String?): EndpointResult?
}
