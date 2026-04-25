package com.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private static final String BASE_URL = "http://103.139.122.250:4000/";

    @BeforeEach
    void setUp() {

        options.addArguments("--remote-allow-origins=*");
        // Setup Chrome options for headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Test login with incorrect credentials")
    void test_login_with_incorrect_credentials() {
        // Navigate to login page
        driver.navigate().to(BASE_URL);
        
        // Wait for page to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Find and fill email field
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
        emailField.sendKeys("qasim@malik.com");
        
        // Find and fill password field
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("abcdefg");
        
        // Click login button
        WebElement loginButton = driver.findElement(By.id("m_login_signin_submit"));
        loginButton.click();
        
        // Wait for error message
        WebElement errorElement = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div/div/div[1]/div/div/div/div[2]/form/div[1]")
        ));
        
        String errorText = errorElement.getText();
        
        // Verify error message
        Assertions.assertTrue(
            errorText.contains("Incorrect email or password"),
            "Expected error message not found. Got: " + errorText
        );
    }

    @Test
    @DisplayName("Test login with empty credentials")
    void test_login_with_empty_credentials() {
        driver.navigate().to(BASE_URL);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Wait for login button and click without entering credentials
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("m_login_signin_submit")));
        loginButton.click();
        
        // Verify we're still on login page (or check for validation message)
        Assertions.assertEquals(BASE_URL, driver.getCurrentUrl(), "Should remain on login page");
    }

    @Test
    @DisplayName("Test login page loads successfully")
    void test_login_page_loads() {
        driver.navigate().to(BASE_URL);
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Verify email field exists
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));
        Assertions.assertTrue(emailField.isDisplayed(), "Email field should be visible");
        
        // Verify password field exists
        WebElement passwordField = driver.findElement(By.name("password"));
        Assertions.assertTrue(passwordField.isDisplayed(), "Password field should be visible");
        
        // Verify login button exists
        WebElement loginButton = driver.findElement(By.id("m_login_signin_submit"));
        Assertions.assertTrue(loginButton.isDisplayed(), "Login button should be visible");
    }
}