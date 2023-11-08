package com.example.unittestingcourseinkotlin.screens.common.controllers

interface BackPressDispatcher {
    fun registerListener(listener: BackPressedListener?)
    fun unregisterListener(listener: BackPressedListener?)
}
