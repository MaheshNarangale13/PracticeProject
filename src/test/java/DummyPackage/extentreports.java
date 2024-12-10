package DummyPackage;

	import java.io.File;
	import java.time.Duration;
	import java.text.SimpleDateFormat;
	import java.util.Date;

	import org.openqa.selenium.By;
	import org.openqa.selenium.Keys;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.openqa.selenium.support.ui.ExpectedConditions;
	import org.openqa.selenium.support.ui.WebDriverWait;

	import io.github.bonigarcia.wdm.WebDriverManager;
	import org.testng.Assert;
	import org.testng.annotations.AfterClass;
	import org.testng.annotations.BeforeClass;
	import org.testng.annotations.Test;

	import com.aventstack.extentreports.ExtentReports;
	import com.aventstack.extentreports.ExtentTest;
	import com.aventstack.extentreports.reporter.ExtentSparkReporter;

	public class extentreports 
	{
	    private WebDriver driver;
	    private static ExtentReports extent;
	    private static ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();
	    private static String reportPath;
	    private String expectedErrorMessage = "You are not registered with us. Please sign up.";

	    @BeforeClass
	    public void setUp() 
	    {
	        // Initialize ExtentReports
	        extent = setupExtentReports();

	        // Setup WebDriver
	        WebDriverManager.chromedriver().setup();
	        driver = new ChromeDriver();
	        driver.manage().window().maximize();
	    }

	    @Test
	    public void testLoginWithWrongNumber() {
	        ExtentTest test = startTest("Test Login With Wrong Number");
	        testReport.set(test);

	        try {
	            // Navigate to Google
	            driver.get("https://www.google.com");
	            logStatus("info", "Navigated to Google");

	            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	            // Search for Flipkart login page
	            WebElement searchInput = driver.findElement(By.name("q"));
	            searchInput.sendKeys("flipkart login", Keys.RETURN);
	            logStatus("info", "Searched for Flipkart login page");

	            // Click on the Flipkart link
	            WebElement flipkartSite = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3[normalize-space()='Flipkart']")));
	            flipkartSite.click();
	            logStatus("info", "Clicked on Flipkart link");

	            // Click on the Login button
	            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Login']")));
	            loginButton.click();
	            logStatus("info", "Clicked on Login button");

	            // Enter unregistered phone number
	            WebElement flipkartLogin = driver.findElement(By.xpath("//input[@class='r4vIwl BV+Dqf']"));
	            flipkartLogin.sendKeys("9511883017");
	            logStatus("info", "Entered unregistered phone number");

	            // Click on Request OTP
	            driver.findElement(By.xpath("//button[normalize-space()='Request OTP']")).click();
	            logStatus("info", "Clicked on Request OTP button");

	            // Wait for error message and fetch it
	            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
	                    By.xpath("//div[@class='eIDgeN' and text()='You are not registered with us. Please sign up.']")
	            ));

	            String errorText = errorMessage.getText();
	            logStatus("info", "Captured error message: " + errorText);

	            // Assert the error message matches the expected value
	            Assert.assertEquals(errorText, expectedErrorMessage, "Error message mismatch!");
	            logStatus("pass", "Test passed with expected error message");

	        } catch (Exception e) {
	            logStatus("fail", "Test failed due to exception: " + e.getMessage());
	            e.printStackTrace();
	            Assert.fail("Test failed due to exception: " + e.getMessage());
	        }
	    }

	    @AfterClass
	    public void tearDown() {
	        // Flush the Extent Report
	        if (extent != null) {
	            extent.flush();
	        }

	        // Close the browser
	        if (driver != null) {
	            driver.quit();
	        }
	    }

	    // Setup ExtentReports with dynamic file creation
	    private static ExtentReports setupExtentReports() {
	        if (extent == null) {
	            File reportDir = new File(System.getProperty("user.dir") + "/test-output/extent-reports/");
	            if (!reportDir.exists()) {
	                reportDir.mkdirs();
	            }
	            reportPath = reportDir + "/TestReport_" + getTimeStamp() + ".html";

	            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
	            extent = new ExtentReports();
	            extent.attachReporter(sparkReporter);
	            extent.setSystemInfo("OS", System.getProperty("os.name"));
	            extent.setSystemInfo("Environment", "Test Automation");
	        }
	        return extent;
	    }

	    // Start a test with a given name
	    private static ExtentTest startTest(String testName) {
	        ExtentTest test = extent.createTest(testName);
	        testReport.set(test);
	        return test;
	    }

	    // Log status into ExtentReports
	    private static void logStatus(String status, String message) {
	        ExtentTest test = testReport.get();
	        if (test != null) {
	            switch (status.toLowerCase()) {
	                case "pass":
	                    test.pass(message);
	                    break;
	                case "fail":
	                    test.fail(message);
	                    break;
	                case "skip":
	                    test.skip(message);
	                    break;
	                default:
	                    test.info(message);
	            }
	        }
	    }

	    // Generate a timestamp for unique file names
	    private static String getTimeStamp() {
	        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    }
	}

