package com.example.unit_testing_fundamentals.exercise1

class NegativeNumberValidator {
    fun isNegative(number: Int): Boolean {
        // the bug is that 0 will be reported as negative while it's not
        return number <= 0
    }
}
