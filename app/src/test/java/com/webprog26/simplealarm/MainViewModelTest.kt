package com.webprog26.simplealarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `onAddAlarmButtonClick sets observable data value to true`() {
        val mainViewModel = MainViewModel()

        mainViewModel.onAddAlarmButtonClick()

        Assert.assertEquals(mainViewModel.mButtonClicked.getValue(), true)
    }

}