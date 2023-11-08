package com.example.unitTestingInAndroid.example13

import android.text.TextUtils

class AndroidUnitTestingProblems {
    fun callStaticAndroidApi(string: String?) {
        TextUtils.isEmpty(string)
    }
}
