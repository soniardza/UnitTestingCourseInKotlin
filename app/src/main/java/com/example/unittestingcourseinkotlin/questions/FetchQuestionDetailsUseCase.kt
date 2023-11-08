package com.example.unittestingcourseinkotlin.questions

import com.example.unittestingcourseinkotlin.common.BaseObservable
import com.example.unittestingcourseinkotlin.common.time.TimeProvider
import com.example.unittestingcourseinkotlin.networking.questions.FetchQuestionDetailsEndpoint
import com.example.unittestingcourseinkotlin.networking.questions.QuestionSchema

class FetchQuestionDetailsUseCase(
    private val mFetchQuestionDetailsEndpoint: FetchQuestionDetailsEndpoint,
    private val mTimeProvider: TimeProvider,
) : BaseObservable<FetchQuestionDetailsUseCase.Listener?>() {
    interface Listener {
        fun onQuestionDetailsFetched(questionDetails: QuestionDetails?)
        fun onQuestionDetailsFetchFailed()
    }

    private val mQuestionDetailsCache: MutableMap<String, QuestionDetailsCacheEntry> = HashMap()
    fun fetchQuestionDetailsAndNotify(questionId: String) {
        if (serveQuestionDetailsFromCacheIfValid(questionId)) {
            return
        }
        mFetchQuestionDetailsEndpoint.fetchQuestionDetails(
            questionId,
            object : FetchQuestionDetailsEndpoint.Listener {
                override fun onQuestionDetailsFetched(question: QuestionSchema?) {
                    val cacheEntry = QuestionDetailsCacheEntry(
                        schemaToQuestionDetails(question),
                        mTimeProvider.currentTimestamp,
                    )
                    mQuestionDetailsCache[questionId] = cacheEntry
                    notifySuccess(cacheEntry.mQuestionDetails)
                }

                override fun onQuestionDetailsFetchFailed() {
                    notifyFailure()
                }
            },
        )
    }

    private fun serveQuestionDetailsFromCacheIfValid(questionId: String): Boolean {
        val cachedQuestionDetailsEntry = mQuestionDetailsCache[questionId]
        return if (cachedQuestionDetailsEntry != null &&
            mTimeProvider.currentTimestamp < cachedQuestionDetailsEntry.mCachedTimestamp + CACHE_TIMEOUT_MS
        ) {
            notifySuccess(cachedQuestionDetailsEntry.mQuestionDetails)
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
        for (listener in listeners) {
            listener?.onQuestionDetailsFetchFailed()
        }
    }

    private fun notifySuccess(questionDetails: QuestionDetails) {
        for (listener in listeners) {
            listener?.onQuestionDetailsFetched(questionDetails)
        }
    }

    class QuestionDetailsCacheEntry(
        val mQuestionDetails: QuestionDetails,
        val mCachedTimestamp: Long,
    )

    companion object {
        private const val CACHE_TIMEOUT_MS: Long = 60000
    }
}
