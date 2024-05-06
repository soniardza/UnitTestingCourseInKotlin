package com.example.tutorialAndroidApplication.common.dependencyinjection

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.tutorialAndroidApplication.common.time.TimeProvider
import com.example.tutorialAndroidApplication.networking.StackoverflowApi
import com.example.tutorialAndroidApplication.networking.questions.FetchLastActiveQuestionsEndpoint
import com.example.tutorialAndroidApplication.questions.FetchLastActiveQuestionsUseCase
import com.example.tutorialAndroidApplication.questions.FetchQuestionDetailsUseCase
import com.example.tutorialAndroidApplication.screens.common.ViewMvcFactory
import com.example.tutorialAndroidApplication.screens.common.controllers.BackPressDispatcher
import com.example.tutorialAndroidApplication.screens.common.fragmentframehelper.FragmentFrameHelper
import com.example.tutorialAndroidApplication.screens.common.fragmentframehelper.FragmentFrameWrapper
import com.example.tutorialAndroidApplication.screens.common.navdrawer.NavDrawerHelper
import com.example.tutorialAndroidApplication.screens.common.screensnavidator.ScreensNavigator
import com.example.tutorialAndroidApplication.screens.common.toastshelper.ToastsHelper
import com.example.tutorialAndroidApplication.screens.questiondetails.QuestionDetailsController
import com.example.tutorialAndroidApplication.screens.questionslist.QuestionsListController

class ControllerCompositionRoot(
    private val compositionRoot: CompositionRoot,
    private val activity: FragmentActivity,
) {
    private fun getActivity(): FragmentActivity {
        return activity
    }

    private fun getContext(): Context {
        return activity
    }

    private fun getFragmentManager(): FragmentManager {
        return getActivity().supportFragmentManager
    }

    private fun getStackoverflowApi(): StackoverflowApi {
        return compositionRoot.getStackoverflowApi()
    }

    private fun getFetchLastActiveQuestionsEndpoint(): FetchLastActiveQuestionsEndpoint {
        return FetchLastActiveQuestionsEndpoint(getStackoverflowApi())
    }

    private fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(getContext())
    }

    fun getViewMvcFactory(): ViewMvcFactory {
        return ViewMvcFactory(getLayoutInflater(), getNavDrawerHelper())
    }

    private fun getNavDrawerHelper(): NavDrawerHelper {
        return getActivity() as NavDrawerHelper
    }

    fun getFetchQuestionDetailsUseCase(): FetchQuestionDetailsUseCase {
        return compositionRoot.getFetchQuestionDetailsUseCase()
    }

    fun getFetchLastActiveQuestionsUseCase(): FetchLastActiveQuestionsUseCase {
        return FetchLastActiveQuestionsUseCase(getFetchLastActiveQuestionsEndpoint())
    }

    fun getTimeProvider(): TimeProvider {
        return compositionRoot.getTimeProvider()
    }

    fun getQuestionsListController(): QuestionsListController {
        return QuestionsListController(
            getFetchLastActiveQuestionsUseCase(),
            getScreensNavigator(),
            getToastsHelper(),
            getTimeProvider(),
        )
    }

    fun getToastsHelper(): ToastsHelper {
        return ToastsHelper(getContext())
    }

    fun getScreensNavigator(): ScreensNavigator {
        return ScreensNavigator(getFragmentFrameHelper())
    }

    private fun getFragmentFrameHelper(): FragmentFrameHelper {
        return FragmentFrameHelper(getActivity(), getFragmentFrameWrapper(), getFragmentManager())
    }

    private fun getFragmentFrameWrapper(): FragmentFrameWrapper {
        return getActivity() as FragmentFrameWrapper
    }

    fun getBackPressDispatcher(): BackPressDispatcher {
        return getActivity() as BackPressDispatcher
    }

    fun getQuestionDetailsController(): QuestionDetailsController {
        return QuestionDetailsController(
            getFetchQuestionDetailsUseCase(),
            getScreensNavigator(),
            getToastsHelper(),
        )
    }
}
