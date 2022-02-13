
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

public class TestUpdateCertActivitySelenium  {
	WebDriver driver;
	
	private static final String PARTNERNAME = "raggiver@gmail.com";
	private static final String PASS = "forzaLazio";
	private static final String LOGIN_URL = "http://localhost:8080/Dovado/loginPartner.jsp";
	
	private static final String DRIVER_PATH = "webdriver.chrome.driver";
	private static final String DRIVER_PROPERTY = "Drivers/chromedriver";
		
		
	@Test
	public void test() {
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);
		
		driver = new ChromeDriver();
		driver.get(LOGIN_URL);
		
		driver.findElement(By.cssSelector("[name=email]")).sendKeys(PARTNERNAME);
		driver.findElement(By.cssSelector("[name=password]")).sendKeys(PASS);
		driver.findElement(By.cssSelector("[type=submit]")).click();
		
		driver.findElement(By.cssSelector(".scheduledActivityCards")).click();
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-secondary")));
		
		driver.findElement(By.cssSelector(".btn-Secondary")).click();
		driver.findElement(By.cssSelector("#scheduleModal [type=submit]")).click();
		
		wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[name=discount5]")));
		
		driver.findElement(By.cssSelector("[name=discount5]")).click();
		driver.findElement(By.cssSelector("[type=submit]")).click();
		boolean test = driver.findElement(By.cssSelector("#responseModal > div > div > div.modal-body > h5")).equals("Sconti correttamente modificati!");
		
		assertEquals(true, test);
		}
}
