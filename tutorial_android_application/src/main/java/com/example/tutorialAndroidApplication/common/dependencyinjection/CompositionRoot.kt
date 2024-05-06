package com.example.tutorialAndroidApplication.common.dependencyinjection

import com.example.tutorialAndroidApplication.common.Constants
import com.example.tutorialAndroidApplication.common.time.TimeProvider
import com.example.tutorialAndroidApplication.networking.StackoverflowApi
import com.example.tutorialAndroidApplication.networking.questions.FetchQuestionDetailsEndpoint
import com.example.tutorialAndroidApplication.questions.FetchQuestionDetailsUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CompositionRoot {
    private var retrofit: Retrofit? = null
    private var fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase? = null

    private fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit =
                Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!
    }

    fun getStackoverflowApi(): StackoverflowApi {
        return getRetrofit().create(StackoverflowApi::class.java)
    }

    fun getTimeProvider(): TimeProvider {
        return TimeProvider()
    }

    private fun getFetchQuestionDetailsEndpoint(): FetchQuestionDetailsEndpoint {
        return FetchQuestionDetailsEndpoint(getStackoverflowApi())
    }

    fun getFetchQuestionDetailsUseCase(): FetchQuestionDetailsUseCase {
        if (fetchQuestionDetailsUseCase == null) {
            fetchQuestionDetailsUseCase =
                FetchQuestionDetailsUseCase(getFetchQuestionDetailsEndpoint(), getTimeProvider())
        }
        return fetchQuestionDetailsUseCase!!
    }
}
