package com.rikucherry.artworkespresso.feature_authentication.presentation

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EntranceActivityTest {

    @get: Rule
    val composeTestRule = createAndroidComposeRule<EntranceActivity>()

    @Before
    fun setup(){
        Intents.init()
    }

    @After
    fun tearUp() {
        Intents.release()
    }

    @Test
    fun `OnCreate_InitialDisplay_ShowProperUI`() {
        val buttonPrimary = composeTestRule.onNode(hasTestTag("menuButtonPrimary"))
        val buttonSecondary = composeTestRule.onNode(hasTestTag("menuButtonSecondary"))

        buttonPrimary.assertIsDisplayed()
        buttonSecondary.assertIsDisplayed()
    }


    @Test
    fun `OnClick_PrimaryButton_LaunchBrowser`() {
        val buttonPrimary = composeTestRule.onNode(hasTestTag("menuButtonPrimary"))

        buttonPrimary.performClick()
        intended(hasAction(Intent.ACTION_VIEW))
    }

}