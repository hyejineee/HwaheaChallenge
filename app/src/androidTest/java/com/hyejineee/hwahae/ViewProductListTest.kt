package com.hyejineee.hwahae

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.hyejineee.hwahae.views.MainActivity
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
        onView(withText("모든 피부 타입")).check(matches(isDisplayed()))
        onView(withText("플라멜엠디 밀크러스트필 마일드 워시오프 앰플 5ml x 2개")).check(matches(isDisplayed()))
    }
}