package com.example.unittestingcourseinkotlin.common.dependencyinjection

import com.example.unittestingcourseinkotlin.common.Constants
import com.example.unittestingcourseinkotlin.common.time.TimeProvider
import com.example.unittestingcourseinkotlin.networking.StackoverflowApi
import com.example.unittestingcourseinkotlin.networking.questions.FetchQuestionDetailsEndpoint
import com.example.unittestingcourseinkotlin.questions.FetchQuestionDetailsUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CompositionRoot {
    private var mRetrofit: Retrofit? = null
    private var mFetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase? = null
    private val retrofit: Retrofit?
        private get() {
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return mRetrofit
        }
    val stackoverflowApi: StackoverflowApi
        get() = retrofit!!.create(StackoverflowApi::class.java)
    val timeProvider: TimeProvider
        get() = TimeProvider()
    private val fetchQuestionDetailsEndpoint: FetchQuestionDetailsEndpoint
        private get() = FetchQuestionDetailsEndpoint(stackoverflowApi)
    val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase
        get() {
            if (mFetchQuestionDetailsUseCase == null) {
                mFetchQuestionDetailsUseCase = FetchQuestionDetailsUseCase(
                    fetchQuestionDetailsEndpoint,
                    timeProvider,
                )
            }
            return mFetchQuestionDetailsUseCase!!
        }
}
