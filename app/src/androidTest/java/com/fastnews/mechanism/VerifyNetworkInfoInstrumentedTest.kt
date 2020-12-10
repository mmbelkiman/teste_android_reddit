package com.fastnews.mechanism

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.fastnews.mechanism.VerifyNetworkInfo

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class VerifyNetworkInfoInstrumentedTest {
    @Test
    fun isConnected() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val mReturn = VerifyNetworkInfo.isConnected(appContext)
        assertEquals(true, mReturn)
    }
}
