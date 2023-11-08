package com.example.testDoublesFundamentals.example6

class Counter private constructor() {
    var total = 0
        private set

    fun add() {
        total++
    }

    fun add(count: Int) {
        total += count
    }

    companion object {
        private var sInstance: Counter? = null

        /**
         * @return reference to Counter Singleton
         */
        val instance: Counter?
            get() {
                if (sInstance == null) {
                    sInstance = Counter()
                }
                return sInstance
            }
    }
}
