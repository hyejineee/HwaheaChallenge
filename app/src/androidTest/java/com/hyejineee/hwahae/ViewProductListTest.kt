package com.hyejineee.hwahae

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.ActivityAction
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.hyejineee.hwahae.views.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class ViewProductListTest {
    @get:Rule
    val mainActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun viewDefaultProductList(){
        Thread.sleep(1000)
        onView(withText("모든 피부 타입")).check(matches(isDisplayed()))
        onView(withText("플라멜엠디 밀크러스트필 마일드 워시오프 앰플 5ml x 2개")).check(matches(isDisplayed()))
    }

    @Test
    fun viewFilteringProductList(){
        onView(withId(R.id.skin_type_sp)).perform(click())

        onView(withText("민감성")).check(matches(isDisplayed()))
        onView(withText("민감성")).perform(click())

        Thread.sleep(1000)
        onView(withText("엔오에이치제이 코리안 에스테틱 마스크 포어버블")).check(matches(isDisplayed()))
    }
}