package miniProject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.ITestResult;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.apache.commons.io.FileUtils;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SearchAndFilterTest {
	
	
	// DECLARING THREAD-SAFE DRIVER, WAIT, SOFTASSERT OBJECT
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();
	private static ThreadLocal<SoftAssert> softAssert = new ThreadLocal<>();
	
	// DECLARING STATIC VARIABLE SO THAT BOTH THREAD USE SAME OBJECTS
	static XSSFWorkbook workbook;
	static XSSFSheet sheet;
	static int rowNum = 0;
	
	String searchQuery = "mobile smartphones under 30000";
	String ssfileLocation = "C:/Users/2407391/eclipse-workspace/Mini_project/ScreenShots/";
	String excelFileLocation = "C:/Users/2407391/eclipse-workspace/Mini_project/ExcelReports/";

	public void takeScreenShot() throws IOException {
		// NAMEING SCREENSHOTS BASED ON CURRENT TIME
		String fileName =  new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".png";
		
		TakesScreenshot screenshotTaker = (TakesScreenshot) getDriver();
		File ss = screenshotTaker.getScreenshotAs(OutputType.FILE);
		File dstn = new File(ssfileLocation + fileName);
		FileUtils.copyFile(ss, dstn);
		
	}
	
	
	@Parameters("browser")
	@BeforeTest
	public void setUp(String browserName) {
		
		// CREATING WEBDRIVER BASED ON BROWSER NAME PASSED IN PARAMETER
		if(browserName.equalsIgnoreCase("Chrome")) {
			driver.set(new ChromeDriver());
		}else if(browserName.equalsIgnoreCase("Edge")){
			driver.set(new EdgeDriver());
		}
		
		//WORKBOOK OJECT SHOULD BE CREATED ONLY ONCE
		if(workbook == null) {
			
			workbook = new XSSFWorkbook();
			sheet = workbook.createSheet("Test Result");
			XSSFRow titleRow= sheet.createRow(rowNum++);
			
			titleRow.createCell(0).setCellValue("Browser Name");
			titleRow.createCell(1).setCellValue("Test Name");
			titleRow.createCell(2).setCellValue("Test Result");
			
		}
		
	}
	
	// FUNCTION TO ACCESS THE WEBDRIVER
	public WebDriver getDriver() {
		return driver.get();
	}
	
//	FUNCTION TO ACCESS THE WAIT
	public WebDriverWait getWait() {
		return wait.get();
	}
	
//	FUNCTION TO ACCESS THE SOFTASSERT
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	
	@Parameters("browser")
	@Test(priority = 1)
	public void openBrowser(String browser) {
		
		// OPEN THE AMAZON WEBSITE
		getDriver().get("https://www.amazon.in/");
		getDriver().manage().window().maximize();
	
		wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(5)));
		softAssert.set(new SoftAssert());

	}
	
	@Test(priority = 2, enabled = true)
	public void searchTest() throws IOException {
		// ENTER THE SEACH QUERY IN SEARCH BOX
		WebElement searchBar = getDriver().findElement(By.id("twotabsearchtextbox"));
		searchBar.sendKeys(searchQuery);
		WebElement searchBtn = getDriver().findElement(By.id("nav-search-submit-button"));
		searchBtn.click();
		
		 // WAITING FOR RESULT PAGE TO LOAD SUCCESSFULLY
        getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Results']")));
		
        // TAKE SCREENSHOT OF SEARCH RESULT
        takeScreenShot();
		
	}
	
	@Test(priority = 3, enabled = true)
	public void searchMsgTest() {
		// VALIDATE THE TEXT OF SEARCH MESSAGE
		String searchMsg = getDriver().findElement(By.xpath("//h2[1]/span[1]")).getText();
		String searchOp = getDriver().findElement(By.xpath("//h2[1]/span[3]")).getText();
		
		getSoftAssert().assertTrue(searchMsg.contains("of over"), "search message does not match");
		getSoftAssert().assertTrue(searchMsg.contains("result for"), "search message does not match");
		getSoftAssert().assertTrue(searchOp.equalsIgnoreCase(searchQuery), "search Query does not match");
		
		
	}
	
	@Test(priority = 4, enabled = true)
	public void checkSortTest() throws IOException {
		// CLICK ON SORT DROPDOWN MENU
		WebElement sortEle = getDriver().findElement(By.xpath("//span[@id='a-autoid-0']"));
		sortEle.click();
		
		// VERIFY THAT 5 OPTIONS ARE AVAILABLE TO SORT THE SEARCH RESULT 
		List<WebElement> sortOptions = getDriver().findElements(By.xpath("//li[@role='option']"));
		int sortOptionsSize = sortOptions.size();
		getSoftAssert().assertTrue(sortOptionsSize == 6, "Sort Options count mismatch");
		
		
		// CLICK ON "NEWEST ARRIVAL"
		WebElement newestArrivalsEle = getWait().until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Newest Arrivals']")));
		newestArrivalsEle.click();
		getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Results']")));
		
		// TAKE SCREENSHOT OF SORTED SEARCH RESULT
		takeScreenShot();
		
		// VERIFY THAT "NEWEST ARRIVAL" IS SELECTED
		String curSortOption = getDriver().findElement(By.xpath("//span[@class='a-dropdown-prompt']")).getText();
		getSoftAssert().assertTrue(curSortOption.equalsIgnoreCase("Newest Arrivals"), "Selected Sort Option mismatch");
		
		
	}
	
	@AfterMethod
	@Parameters("browser")
	public void logTestResult(String browserName, ITestResult result) {
		// SYNCHRONIZED BLOCK ENSURE ONLY ONE THREAD CAN WRITE IN EXCEL FILE AT TIME
		synchronized (SearchAndFilterTest.class) {
		
		XSSFRow row = sheet.createRow(rowNum++);
		row.createCell(0).setCellValue(browserName);
		
		// FETCH THE CURRENT TEST NAME
		String testName = result.getMethod().getMethodName();
		
		row.createCell(1).setCellValue(testName);
		
		String testResult = "";
		 
		// FETCH THE CURRECT TEST STATUS
		switch(result.getStatus()) {
			case ITestResult.SUCCESS : testResult = "PASS"; break;
			case ITestResult.FAILURE : testResult = "Fail"; break;
			case ITestResult.SKIP : testResult = "SKIP"; break;
		}
		
		row.createCell(2).setCellValue(testResult);
		}
	}
	
	
	@AfterClass
	public void tearDown() throws IOException {
		
		//SAFELY RELEASE RESOURCES USED AFTER EXECUTION
		getDriver().close();
		driver.remove();
		wait.remove();
		softAssert.remove();
	}
	
	@AfterSuite
	public void writeLogExcel() throws IOException {
		
		// WRITE THE EXCEL IN ROM 
		// EXCEL FILE IS NAMED BASED ON CURRENT TIME
		String fileName =  new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".xlsx";
		
		File file = new File(excelFileLocation + fileName);
		FileOutputStream out = new FileOutputStream(file);
		
		// SAFELY RELEASE RESOURCES USED AFTER EXECUTION
		workbook.write(out);
		out.close();
		
	}

}
