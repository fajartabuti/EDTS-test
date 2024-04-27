import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import internal.GlobalVariable as GlobalVariable
import io.appium.java_client.android.AndroidDriver as AndroidDriver
import io.appium.java_client.android.nativekey.AndroidKey as AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent as KeyEvent

AndroidDriver<?> driver = MobileDriverFactory.getDriver()

'expect homepage is appear'
Mobile.verifyElementVisible(findTestObject('Object Repository/Home Page/navBar_beranda'), 0)
'tap \'search product\' searchbox'
Mobile.tap(findTestObject('Home Page/searchBox'), 0)
'set product search box element'
def searchProductSearchBox = findTestObject('Object Repository/Product Detail Page/searchView')
'expect redirect to search product page'
Mobile.verifyElementExist(searchProductSearchBox, 0)
'get list of products to be searching for'
Map listOfProducts = products
'open product detail and add product to cart'
Object productSearchName = listOfProducts.keySet().toArray()
'define subtotal of products that added to cart'
def subTotalProducts = 0

for (i = 0; i < (listOfProducts.size()); i++) {
	'tap searchbox'
	Mobile.tap(searchProductSearchBox, 0)	
    'input product name'
    Mobile.setText(searchProductSearchBox, productSearchName[i], 0)
	'tap first suggestion list'
    Mobile.tap(findTestObject('Object Repository/Product Search Page/suggestionView_first_suggestion'), 0)
	'wait for redirect to product list page'
	Mobile.waitForElementPresent(findTestObject('Object Repository/Product Search Page/cardContent_product'), 0)
	'tap first product on the product list page'
    Mobile.tap(findTestObject('Object Repository/Product Search Page/cardContent_product'), 0)
	'expect redirect to product detail page'
    Mobile.verifyElementExist(findTestObject('Object Repository/_Parameterize Selector/class_contain_text', [('class') : '*'
                , ('text') : 'Deskripsi Produk']), 0)

	'define product element'
    def productNameElem = findTestObject('Object Repository/Product Detail Page/productName')
    def productName = Mobile.getAttribute(productNameElem, 'text', 0)
    def productPriceElem = findTestObject('Object Repository/Product Detail Page/productPrice')
    def productPrice = Mobile.getAttribute(productPriceElem, 'text', 0)
    productPrice = Integer.parseInt(productPrice.replace('Rp ', '').replace('.', ''))

	'tap add to cart button'
    Mobile.tap(findTestObject('Object Repository/Product Detail Page/btnAddToCart'), 0)

	'define product qty'
    Object productQty = listOfProducts.get(productSearchName[i])

    for (j = 1; j < productQty; j++) {
        Mobile.tap(findTestObject('Object Repository/Product Detail Page/textViewAddProductQty'), 0)

        Mobile.verifyElementExist(findTestObject('Object Repository/Product Detail Page/viewSubTotal'), 0)
    }
    
	'get product subtotal'
	def productSubTotalElem = findTestObject('Object Repository/Product Detail Page/viewSubTotal')
    def productSubTotal = Mobile.getAttribute(productSubTotalElem, 'text', 0)
    productSubTotal = Integer.parseInt(productSubTotal.replace('Rp ', '').replace('.', ''))

	'expect equal product subtotal with product added to cart'
    assert productSubTotal == productPrice * productQty
	
	'set subtotal of products'
	subTotalProducts = subTotalProducts + productSubTotal
	
	'add another products condition'
	if(i != listOfProducts.size() - 1) {
		Mobile.pressBack()
		Mobile.tap(searchProductSearchBox, 0)
		Mobile.clearText(searchProductSearchBox, 0)
	}
}
'go to \'keranjang page\''
def toCartPageElem = findTestObject('Object Repository/Product Detail Page/btnToCartPage')
Mobile.tap(toCartPageElem, 0)
Mobile.verifyElementExist(findTestObject('Object Repository/_Parameterize Selector/class_contain_text', [('class') : 'android.widget.TextView'
	, ('text') : 'Keranjang Belanja']), 0)

'choose shipping method'
def btnChooseShippingElem = findTestObject('Object Repository/Cart Page/btnChooseShipping')
Mobile.waitForElementPresent(btnChooseShippingElem, 0)
Mobile.tap(btnChooseShippingElem, 0)

def shippingMethodElem = findTestObject('Object Repository/Cart Page/btnShippingMethod', [('shippingMethod') : "${shippingMethod}"])
Mobile.waitForElementPresent(shippingMethodElem, 0)
Mobile.tap(shippingMethodElem, 0)
Mobile.tap(findTestObject('Object Repository/_Parameterize Selector/class_contain_text', [('class') : 'android.widget.TextView'
	, ('text') : 'Konfirmasi']), 0)

'calculate summary'
def btnTotalElem = findTestObject('Object Repository/Cart Page/btnTotal')
Mobile.tap(btnTotalElem, 0)
Mobile.verifyElementExist(findTestObject('Object Repository/_Parameterize Selector/class_contain_text', [('class') : 'android.widget.TextView'
		, ('text') : 'Ringkasan Belanja']), 0)

def subtotalElem = findTestObject('Object Repository/Cart Page/viewSubtotalSummary')
def subtotal = Mobile.getAttribute(subtotalElem, 'text', 0)
subtotal = Integer.parseInt(subtotal.replace('Rp ', '').replace('.', ''))

def deliveryFeeElem = findTestObject('Object Repository/Cart Page/viewDeliveryFeeSummary')
def deliveryFee = Mobile.getAttribute(deliveryFeeElem, 'text', 0)
deliveryFee = Integer.parseInt(deliveryFee.replace('Rp ', '').replace('.', ''))

def totalElem = findTestObject('Object Repository/Cart Page/viewTotalSummary')
def total = Mobile.getAttribute(totalElem, 'text', 0)
total = Integer.parseInt(total.replace('Rp ', '').replace('.', ''))

assert subtotal == subTotalProducts
assert total == subtotal + deliveryFee

Mobile.pressBack()

'tap beli \'button\''
Mobile.tap(findTestObject('Object Repository/Cart Page/btnBuy'), 0)

'expect redirect to pembayaran page and calculate summary'
totalElem = findTestObject('Object Repository/_Parameterize Selector/class_contain_text_following_sibling', [('class') : 'android.widget.TextView'
			, ('text') : 'Total Pembayaran :'])
Mobile.verifyElementExist(totalElem, 0)
Mobile.tap(totalElem, 0)

subtotalElem = findTestObject('Object Repository/_Parameterize Selector/class_contain_text_following_sibling', [('class') : 'android.widget.TextView'
			, ('text') : 'Total Belanja Produk'])
Mobile.verifyElementExist(subtotalElem, 0)
subtotal = Mobile.getAttribute(subtotalElem, 'text', 0)
subtotal = Integer.parseInt(subtotal.replace('Rp ', '').replace('.', ''))

deliveryFeeElem = findTestObject('Object Repository/_Parameterize Selector/class_contain_text_following_sibling', [('class') : 'android.widget.TextView'
			, ('text') : 'Biaya Pengiriman'])
deliveryFee = Mobile.getAttribute(deliveryFeeElem, 'text', 0)
deliveryFee = Integer.parseInt(deliveryFee.replace('Rp ', '').replace('.', ''))

total = Mobile.getAttribute(totalElem, 'text', 0)
total = Integer.parseInt(total.replace('Rp ', '').replace('.', ''))

assert subtotal == subTotalProducts
assert total == subtotal + deliveryFee

'Choose \'Virtual Account\' payment method in \'Pembayaran\' section'
CustomKeywords.'general.Swipe.scrollToElement'("${paymentMethod}", 'text', 'android.widget.TextView', 0)
Mobile.tap(findTestObject('Object Repository/_Parameterize Selector/class_contain_text', [('class') : 'android.widget.TextView'
			, ('text') : "${paymentMethod}"]), 0)

'click \'Bayar Sekarang\' button'
def btnBayarSekarang = findTestObject('Object Repository/_Parameterize Selector/class_contain_text', [('class') : 'android.widget.Button', ('text') : 'Bayar Sekarang'])
Mobile.waitForElementPresent(btnBayarSekarang, 0)
Mobile.tap(btnBayarSekarang, 0)

'Redirect to order success page'
def btnCekStatusPembayaran = findTestObject('Object Repository/_Parameterize Selector/class_contain_text', [('class') : '*', ('text') : 'Cek Status Pembayaran'])
Mobile.verifyElementExist(btnCekStatusPembayaran, 0)

