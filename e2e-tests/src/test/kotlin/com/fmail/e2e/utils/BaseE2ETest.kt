package com.fmail.e2e.utils

import com.microsoft.playwright.*
import com.microsoft.playwright.options.LoadState
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import java.nio.file.Paths

@ExtendWith(ScreenshotOnFailureExtension::class)
abstract class BaseE2ETest {
    companion object {
        lateinit var playwright: Playwright
        lateinit var browser: Browser
        
        @JvmStatic
        @BeforeAll
        fun launchBrowser() {
            playwright = Playwright.create()
            browser = playwright.chromium().launch(
                BrowserType.LaunchOptions()
                    .setHeadless(TestConfig.headless)
                    .setSlowMo(TestConfig.slowMo)
            )
        }
        
        @JvmStatic
        @AfterAll
        fun closeBrowser() {
            browser.close()
            playwright.close()
        }
    }
    
    lateinit var context: BrowserContext
    lateinit var page: Page
    
    @BeforeEach
    fun createContextAndPage() {
        context = browser.newContext(
            Browser.NewContextOptions()
                .setViewportSize(1280, 720)
                .setIgnoreHTTPSErrors(true)
        )
        
        // Set default timeouts
        context.setDefaultTimeout(TestConfig.timeout)
        context.setDefaultNavigationTimeout(TestConfig.navigationTimeout)
        
        page = context.newPage()
        
        // Add console log listener for debugging
        page.onConsoleMessage { msg ->
            if (msg.type() == "error") {
                println("Console error: ${msg.text()}")
            }
        }
    }
    
    @AfterEach
    fun closeContext() {
        context.close()
    }
    
    protected fun navigateToApp() {
        page.navigate(TestConfig.baseUrl)
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
    
    protected fun takeScreenshot(name: String) {
        val path = Paths.get(TestConfig.screenshotPath, "$name.png")
        page.screenshot(Page.ScreenshotOptions().setPath(path))
    }
    
    protected fun waitForElement(selector: String): Locator {
        return page.locator(selector).first()
    }
    
    protected fun clickAndWait(selector: String) {
        page.click(selector)
        page.waitForLoadState(LoadState.NETWORKIDLE)
    }
}

class ScreenshotOnFailureExtension : TestWatcher {
    override fun testFailed(context: ExtensionContext, cause: Throwable?) {
        if (TestConfig.screenshotOnFailure) {
            val testName = context.displayName.replace(" ", "_")
            val timestamp = System.currentTimeMillis()
            
            context.testInstance.ifPresent { instance ->
                if (instance is BaseE2ETest) {
                    try {
                        instance.page.screenshot(
                            Page.ScreenshotOptions().setPath(
                                Paths.get(TestConfig.screenshotPath, "${testName}_failure_$timestamp.png")
                            )
                        )
                    } catch (e: Exception) {
                        println("Failed to take screenshot: ${e.message}")
                    }
                }
            }
        }
    }
}