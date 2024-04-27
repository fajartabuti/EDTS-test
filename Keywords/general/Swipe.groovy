package general

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import org.openqa.selenium.WebElement
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import com.kms.katalon.core.exception.StepFailedException

import internal.GlobalVariable

public class Swipe {
	@Keyword
	def static scrollToElement(String value, String attribute, String className, int scrollElementIndex) throws StepFailedException {
		AndroidDriver<?> driver = MobileDriverFactory.getDriver()
		String context = driver.getContext()

		attribute = attribute == 'content-desc' ? 'descriptionContains' : 'textContains'

		try {
			WebElement element = null

			if (driver instanceof AndroidDriver) {
				String uiSelector = 'new UiSelector().className("' + className + '").' + attribute + '("' + value + '")'
				String uiScrollable = String.format("new UiScrollable(new UiSelector().scrollable(true).instance("+ scrollElementIndex  +")).scrollIntoView(%s)", uiSelector)
				element = driver.findElementByAndroidUIAutomator(uiScrollable)
			}
		}
		finally {
			driver.context(context)
		}
	}
}
