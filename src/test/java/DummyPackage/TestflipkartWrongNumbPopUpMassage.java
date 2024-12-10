package DummyPackage;

import java.time.Duration;
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

public class TestflipkartWrongNumbPopUpMassage 
{

    WebDriver driver;
    String expectedErrorMessage = "You are not registered with us. Please sign up.";

    @BeforeClass
    public void setUp() 
      {
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
      }

    @Test
    public void testLoginWithWrongNumber() 
     {
        try {
            // Navigate to Google
            driver.get("https://www.google.com");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Search for Flipkart login page
            WebElement searchInput = driver.findElement(By.name("q"));
            searchInput.sendKeys("flipkart login", Keys.RETURN);

            // Click on the Flipkart link
            WebElement flipkartSite = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3[normalize-space()='Flipkart']")));
            flipkartSite.click();

            // Click on the Login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Login']")));
            loginButton.click();

            // Enter unregistered phone number
            WebElement flipkartLogin = driver.findElement(By.xpath("//input[@class='r4vIwl BV+Dqf']"));
            flipkartLogin.sendKeys("9511883017");

            // Click on Request OTP
            driver.findElement(By.xpath("//button[normalize-space()='Request OTP']")).click();

            // Wait for error message and fetch it
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='eIDgeN' and text()='You are not registered with us. Please sign up.']")
            ));

            String errorText = errorMessage.getText();
            System.out.println("Error message text: " + errorText);

            // Assert the error message matches the expected value
            Assert.assertEquals(errorText, expectedErrorMessage, "Error message mismatch!");

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        // Close the browser after the test
        if (driver != null) {
            driver.quit();
        }
    }
}
