package com.hyejineee.hwahae

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.hyejineee.hwahae.views.MainActivity
import com.hyejineee.hwahae.views.ProductAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ViewProductDetailDialogTest {

    @get:Rule
    val mainActivity = ActivityTestRule(MainActivity::class.java)

    @Test
    fun viewProductDetailDialog(){
        onView(withText("플라멜엠디 밀크러스트필 마일드 워시오프 앰플 5ml x 2개")).check(matches(isDisplayed()))

        onView(withId(R.id.item_grid_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductAdapter.ViewHolder>(0, click())
        )

        onView(withText("플라멜엠디 밀크러스트필 마일드 워시오프 앰플 5ml x 2개"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        onView(withText("구매하기"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

}