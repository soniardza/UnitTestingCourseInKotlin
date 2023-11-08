package com.example.unittestingcourseinkotlin.common.dependencyinjection

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.unittestingcourseinkotlin.common.time.TimeProvider
import com.example.unittestingcourseinkotlin.networking.StackoverflowApi
import com.example.unittestingcourseinkotlin.networking.questions.FetchLastActiveQuestionsEndpoint
import com.example.unittestingcourseinkotlin.questions.FetchLastActiveQuestionsUseCase
import com.example.unittestingcourseinkotlin.questions.FetchQuestionDetailsUseCase
import com.example.unittestingcourseinkotlin.screens.common.ViewMvcFactory
import com.example.unittestingcourseinkotlin.screens.common.controllers.BackPressDispatcher
import com.example.unittestingcourseinkotlin.screens.common.fragmentframehelper.FragmentFrameHelper
import com.example.unittestingcourseinkotlin.screens.common.fragmentframehelper.FragmentFrameWrapper
import com.example.unittestingcourseinkotlin.screens.common.navdrawer.NavDrawerHelper
import com.example.unittestingcourseinkotlin.screens.common.screensnavigator.ScreensNavigator
import com.example.unittestingcourseinkotlin.screens.common.toastshelper.ToastsHelper
import com.example.unittestingcourseinkotlin.screens.questiondetails.QuestionDetailsController
import com.example.unittestingcourseinkotlin.screens.questionslist.QuestionsListController

class ControllerCompositionRoot(
    private val mCompositionRoot: CompositionRoot,
    private val activity: FragmentActivity,
) {

    private val context: Context
        get() = activity
    private val fragmentManager: FragmentManager
        get() = activity.supportFragmentManager
    private val stackoverflowApi: StackoverflowApi
        get() = mCompositionRoot.stackoverflowApi
    private val fetchLastActiveQuestionsEndpoint: FetchLastActiveQuestionsEndpoint
        get() = FetchLastActiveQuestionsEndpoint(stackoverflowApi)
    private val layoutInflater: LayoutInflater
        get() = LayoutInflater.from(context)
    val viewMvcFactory: ViewMvcFactory
        get() = ViewMvcFactory(layoutInflater, navDrawerHelper)
    private val navDrawerHelper: NavDrawerHelper
        get() = activity as NavDrawerHelper
    val fetchQuestionDetailsUseCase: FetchQuestionDetailsUseCase?
        get() = mCompositionRoot.fetchQuestionDetailsUseCase
    val fetchLastActiveQuestionsUseCase: FetchLastActiveQuestionsUseCase
        get() = FetchLastActiveQuestionsUseCase(fetchLastActiveQuestionsEndpoint)
    val timeProvider: TimeProvider
        get() = mCompositionRoot.timeProvider
    val questionsListController: QuestionsListController
        get() = QuestionsListController(
            fetchLastActiveQuestionsUseCase,
            screensNavigator,
            toastsHelper,
            timeProvider,
        )
    val toastsHelper: ToastsHelper
        get() {
            return ToastsHelper(context)
        }
    val screensNavigator: ScreensNavigator
        get() {
            return ScreensNavigator(fragmentFrameHelper)
        }
    private val fragmentFrameHelper: FragmentFrameHelper
        get() {
            return FragmentFrameHelper(
                activity,
                fragmentFrameWrapper,
                fragmentManager,
            )
        }
    private val fragmentFrameWrapper: FragmentFrameWrapper
        get() {
            return activity as FragmentFrameWrapper
        }
    val backPressDispatcher: BackPressDispatcher
        get() {
            return activity as BackPressDispatcher
        }
    val questionDetailsController: QuestionDetailsController
        get() {
            return QuestionDetailsController(
                (fetchQuestionDetailsUseCase)!!,
                screensNavigator,
                toastsHelper,
            )
        }
}
