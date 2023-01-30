package com.example.chatapplication

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.chatapplication.ui.theme.ChatApplicationTheme
import com.example.chatapplication.view.ChatApp
import com.example.chatapplication.viewmodel.ChatViewModel
import com.example.chatapplication.viewmodel.ImageUIState
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = createComposeRule()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.chatapplication", appContext.packageName)
    }

    @Test
    fun userPromptAsCar(){
        activityRule.setContent {
            ChatApplicationTheme {
                ChatApp()
            }
        }
        activityRule.onNodeWithText("Enter the text to search").performTextInput("car")
        activityRule.onNodeWithText("SEARCH").assertExists().performClick()
    }

    private val testPrompt: List<String> = listOf(
        "dog in house",
        "man on sofa",
        "boy in playground",
        "horse in barn",
        "a white tiger hunting prey",
        "a gold necklace",
        "south indian dish"
    )

    @Test
    fun CheckCurrentPromptInDB(){
        activityRule.setContent {
            ChatApplicationTheme {
                ChatApp()
            }
        }
        val viewModel = ChatViewModel()
//        val text = "tenthouse"
        testPrompt.forEach{text->
            activityRule.onNodeWithText("Enter the text to search").performTextInput(text)
            activityRule.onNodeWithText("SEARCH").assertExists().performClick()
            viewModel.updateCurrentPrompt(text)
            viewModel.searchCurrentPrompt()
            val apiContentDescription = viewModel.CurrentResponseImageState.value.stored_url
            activityRule.waitUntil(timeoutMillis = 10000, condition = {viewModel.imageUIState == ImageUIState.Success})
            activityRule.onNodeWithContentDescription("history", useUnmergedTree = true).assertExists().performClick()
            activityRule.onNodeWithContentDescription(apiContentDescription)
            activityRule.onNodeWithContentDescription("history", useUnmergedTree = true).assertExists().performClick()
        }
    }
}