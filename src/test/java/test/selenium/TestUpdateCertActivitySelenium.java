
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
	
	private static final String PARTNERNAME = "raggivera@gmail.com";
	private static final String PASS = "forzaLazio";
	private static final String LOGIN_URL = "http://localhost:8080/Dovado/loginPartner.jsp";
	
	private static final String DRIVER_PATH = "Drivers/chromedriver";
	private static final String DRIVER_PROPERTY = "webdriver.chrome.driver";
		
		
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
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#activityModal > div > div > div.modal-footer.d-flex.justify-content-center > button.btn.btn-secondary")));
		
		driver.findElement(By.cssSelector("#activityModal > div > div > div.modal-footer.d-flex.justify-content-center > button.btn.btn-secondary")).click();
		
		wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[name=discount5]")));
		
		driver.findElement(By.cssSelector("[name=discount5]")).click();
		driver.findElement(By.cssSelector("#couponModal > div > div > div.modal-footer > button.btn.btn-success")).click();
		boolean test = driver.findElement(By.cssSelector("#responseModal > div > div > div.modal-body > h5")).getText().contains("correttamente");
		
		assertEquals(true, test);
		}
}
