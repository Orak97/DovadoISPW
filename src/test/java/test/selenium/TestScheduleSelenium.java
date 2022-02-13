package test.selenium;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;

public class TestScheduleSelenium {
	WebDriver driver;
	
	private static final String USERNAME = "giovanni.cipriano@gmail.com";
	private static final String PASSWORD = "falessi";
	private static final String LOGIN_URL = "http://localhost:8080/Dovado/login.jsp";
	
	private static final String DRIVER_PATH = "webdriver.chrome.driver";
	private static final String DRIVER_PROPERTY = "Drivers/chromedriver";
	
	
	@Test
	public void test() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		
		driver = new ChromeDriver();
		
		driver.get(LOGIN_URL);
		
		
		driver.findElement(By.cssSelector("[name=email]")).sendKeys(USERNAME);
		driver.findElement(By.cssSelector("[name=password]")).sendKeys(PASSWORD);
		
		driver.findElement(By.cssSelector("[type=submit]")).click();
		
		
		driver.findElement(By.cssSelector("#navbarNav > ul > li:nth-child(3) > a")).click();
		
		driver.findElement(By.cssSelector(".scheduledActivityCards")).click();
		
		WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait2.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[name=scheduledTime]")));
		
		driver.findElement(By.cssSelector("[name=scheduledTime]")).sendKeys("1430");
		driver.findElement(By.cssSelector("#scheduleModal [type=submit]")).click();
		
		Alert alert = driver.switchTo().alert();
		String result =alert.getText();
		assertEquals(result,"Schedulo modificato correttamente!");
		
		
	}

}
