package test.selenium;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestCreateActivitySelenium {

	WebDriver driver;

//---------------------------------LOGIN ELEMENTS------------------------------------------//
	
	private static final String USERNAME = "sav@gmail.com";
	private static final String PASSWORD = "falessi";
		
//---------------------------------ACTIVITY PARAMETERS------------------------------------------//
	private static final String ACTIVITY_NAME = "Test selenium";
	private static final String ACTIVITY_DESCRIPTION = "Tentativo di utilizzo selenium";
	private static final String CITY_NAME = "Roma";
	private static final String OPENING_TIME = "1020";
	private static final String CLOSING_TIME = "2010";

//---------------------------------WEB ELEMENTS------------------------------------------//
	
	private static final String LOGIN_URL = "http://localhost:8614/Dovado/login.jsp";

	private static final String XPATH_USERNAME_TF = "//*[@id=\"emailID\"]";
	private static final String XPATH_PASSWORD_TF = "//*[@id=\"passwordID\"]";
	private static final String XPATH_SIGNIN = "/html/body/div/main/form/button";
	private static final String XPATH_CREATEACTIVITY_BTN = "/html/body/nav/div/div/ul/li[2]/a";
	private static final String XPATH_PLACEFIELD_TF = "//*[@id=\"placeField\"]";
	private static final String XPATH_PLACESEARCH_BTN = "//*[@id=\"search-btn\"]";
	private static final String XPATH_PLACEBOX_BTN = "/html/body/div[1]/div[1]/div[2]/div/div[1]/div";
	private static final String XPATH_ACTIVITYNAME_TF = "//*[@id=\"name\"]";
	private static final String XPATH_ACTIVITYDESCRIPTION_TF = "/html/body/div[1]/div[2]/form/div[3]/textarea";
	private static final String XPATH_OPENINGTIME = "//*[@id=\"openingTime\"]";
	private static final String XPATH_CLOSINGTIME = "//*[@id=\"closingTime\"]";
	
	private static final String ID_ARTE = "Arte";
	private static final String ID_RELAX = "Relax";
	private static final String ID_MUSICA = "Musica";
	private static final String ID_ADRENALINA = "Adrenalina";
	private static final String XPATH_CONTINUE_BTN = "/html/body/div[1]/div[2]/form/div[13]/button";
	
	
	private static final String DRIVER_PATH = "Drivers/chromedriver";
	private static final String DRIVER_PROPERTY = "webdriver.chrome.driver";
	private static final String ARGUMENTS_TXT = "arguments[0].click()";
	
	@Test
	public void test() {
		
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);

		driver = new ChromeDriver();

		driver.get(LOGIN_URL);
		
		//Automatically logs in as an Explorer.
		driver.findElement(By.xpath(XPATH_USERNAME_TF)).sendKeys(USERNAME);
		driver.findElement(By.xpath(XPATH_PASSWORD_TF)).sendKeys("");
		driver.findElement(By.xpath(XPATH_PASSWORD_TF)).sendKeys(PASSWORD);
		driver.findElement(By.xpath(XPATH_SIGNIN)).click();
		
		//Next, after logging in Selenium will click the create activity tab.
		driver.findElement(By.xpath(XPATH_CREATEACTIVITY_BTN)).click();
		
		//Now it will enter a city, click search then select the first place 
		//shown in the results
		driver.findElement(By.xpath(XPATH_PLACEFIELD_TF)).sendKeys(CITY_NAME);
		driver.findElement(By.xpath(XPATH_PLACESEARCH_BTN)).click();
		driver.findElement(By.xpath(XPATH_PLACEBOX_BTN)).click();

		driver.findElement(By.xpath(XPATH_ACTIVITYNAME_TF)).sendKeys(ACTIVITY_NAME);
		driver.findElement(By.xpath(XPATH_ACTIVITYDESCRIPTION_TF)).sendKeys(ACTIVITY_DESCRIPTION);
		
		driver.findElement(By.xpath(XPATH_OPENINGTIME)).sendKeys(OPENING_TIME);
		driver.findElement(By.xpath(XPATH_CLOSINGTIME)).sendKeys(CLOSING_TIME);		

		WebElement checkBoxArt = driver.findElement(By.id(ID_ARTE));
		WebElement checkBoxRelax = driver.findElement(By.id(ID_RELAX));
		WebElement checkBoxMusica = driver.findElement(By.id(ID_MUSICA));
		WebElement checkBoxAdren = driver.findElement(By.id(ID_ADRENALINA));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		js.executeScript(ARGUMENTS_TXT, checkBoxArt);
		js.executeScript(ARGUMENTS_TXT,checkBoxRelax);
		js.executeScript(ARGUMENTS_TXT,checkBoxAdren);
		js.executeScript(ARGUMENTS_TXT,checkBoxMusica);
		
		WebElement contBtn = driver.findElement(By.xpath(XPATH_CONTINUE_BTN));
		js.executeScript(ARGUMENTS_TXT, contBtn);
		
		Alert alert = driver.switchTo().alert();
		
		assertEquals(true, (alert.getText().contains("correttamente") || !alert.getText().contains("non")));
	}
	
	
	
}
