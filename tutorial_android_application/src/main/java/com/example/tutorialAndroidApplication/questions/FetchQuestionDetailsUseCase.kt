package com.example.tutorialAndroidApplication.questions

import com.example.tutorialAndroidApplication.common.BaseObservable
import com.example.tutorialAndroidApplication.common.time.TimeProvider
import com.example.tutorialAndroidApplication.networking.questions.FetchQuestionDetailsEndpoint
import com.example.tutorialAndroidApplication.networking.questions.QuestionSchema

open class FetchQuestionDetailsUseCase(
    private val fetchQuestionDetailsEndpoint: FetchQuestionDetailsEndpoint?,
    private val timeProvider: TimeProvider?,
) : BaseObservable<FetchQuestionDetailsUseCase.Listener?>() {
    interface Listener {
        fun onQuestionDetailsFetched(questionDetails: QuestionDetails)

        fun onQuestionDetailsFetchFailed()
    }

    private val questionDetailsCache = mutableMapOf<String, QuestionDetailsCacheEntry>()

    open fun fetchQuestionDetailsAndNotify(questionId: String) {
        if (serveQuestionDetailsFromCacheIfValid(questionId)) {
            return
        }
        fetchQuestionDetailsEndpoint?.fetchQuestionDetails(
            questionId,
            object : FetchQuestionDetailsEndpoint.Listener {
                override fun onQuestionDetailsFetched(question: QuestionSchema?) {
                    val cacheEntry =
                        timeProvider?.getCurrentTimestamp()?.let {
                            QuestionDetailsCacheEntry(
                                schemaToQuestionDetails(question),
                                it,
                            )
                        }
                    questionDetailsCache[questionId] = cacheEntry as QuestionDetailsCacheEntry
                    notifySuccess(cacheEntry.questionDetails)
                }

                override fun onQuestionDetailsFetchFailed() {
                    notifyFailure()
                }
            },
        )
    }

    private fun serveQuestionDetailsFromCacheIfValid(questionId: String): Boolean {
        val cachedQuestionDetailsEntry = questionDetailsCache[questionId]
        return if (cachedQuestionDetailsEntry != null &&
            timeProvider?.getCurrentTimestamp()!! < cachedQuestionDetailsEntry.cachedTimestamp + CACHE_TIMEOUT_MS
        ) {
            notifySuccess(cachedQuestionDetailsEntry.questionDetails)
            true
        } else {
            false
        }
    }

    private fun schemaToQuestionDetails(questionSchema: QuestionSchema?): QuestionDetails {
        return QuestionDetails(
            questionSchema!!.id,
            questionSchema.title,
            questionSchema.body,
        )
    }

    private fun notifyFailure() {
        for (listener in getListeners) {
            listener?.onQuestionDetailsFetchFailed()
        }
    }

    private fun notifySuccess(questionDetails: QuestionDetails) {
        for (listener in getListeners) {
            listener?.onQuestionDetailsFetched(questionDetails)
        }
    }

    private data class QuestionDetailsCacheEntry(
        val questionDetails: QuestionDetails,
        val cachedTimestamp: Long,
    )

    companion object {
        private const val CACHE_TIMEOUT_MS = 60000
    }
}
