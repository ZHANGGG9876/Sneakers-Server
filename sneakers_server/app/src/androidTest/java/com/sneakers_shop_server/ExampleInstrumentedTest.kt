package com.sneakers_shop_server

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun testNavigationToOrdersFragment() {
        val activityScenario = ActivityScenario.launch(MainActivity2::class.java)
        onView(ViewMatchers.withId(R.id.orderFragment)).perform(click())
        onView(ViewMatchers.withId(R.id.orderFragment)).check(matches(isDisplayed()))
        activityScenario.close()
    }

    @Test
    fun testAddButton() {
        val activityScenario = ActivityScenario.launch(MainActivity2::class.java)
        onView(ViewMatchers.withId(R.id.formFragment)).perform(click())
        onView(ViewMatchers.withId(R.id.btnAdd)).check(matches(isDisplayed()))
        activityScenario.close()
    }

    @Test
    fun testNavigationToDetailFragment() {
        val activityScenario = ActivityScenario.launch(MainActivity2::class.java)
        Thread.sleep(5000)
        onView(ViewMatchers.withId(R.id.recyclerListat)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(3000)
        onView(ViewMatchers.withId(R.id.sneakerImage)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.sneakerGender)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.sneakerName)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.sneakerPrice)).check(matches(isDisplayed()))
        onView(ViewMatchers.withId(R.id.sneakerDescription)).check(matches(isDisplayed()))
        activityScenario.close()
    }


}