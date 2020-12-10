package com.fastnews.mechanism

import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimeElapsedInstrumentedTest {
    @Test
    fun getTimeElapsed() {
        val mReturn = TimeElapsed.getTimeElapsed(200)
        assertEquals("18606 days", mReturn)
    }
}
