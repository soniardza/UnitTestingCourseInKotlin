package com.example.testDrivenDevelopment.exercise8.networking

interface GetContactsHttpEndpoint {
    enum class FailReason {
        GENERAL_ERROR, NETWORK_ERROR
    }

    interface Callback {
        fun onGetContactsSucceeded(cartItems: List<ContactSchema?>?)
        fun onGetContactsFailed(failReason: FailReason?)
    }

    /**
     * @param filterTerm filter term to match in any of the contact fields
     * @param callback object to be notified when the request completes
     */
    fun getContacts(filterTerm: String?, callback: Callback?)
}
