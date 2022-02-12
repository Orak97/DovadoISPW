package test.selenium;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestCreateActivitySelenium {

	WebDriver driver;

//---------------------------------LOGIN ELEMENTS------------------------------------------//
	
	private static final String USERNAME = "sav@gmail.com";
	private static final String PASSWORD = "falessi";
	private static final String ACTIVITY_NAME = "Test selenium";
	private static final String ACTIVITY_DESCRIPTION = "Tentativo di utilizzo selenium";
	private static final String LOGIN_URL = "";
	private static final String CITY_NAME = "Roma";
	private static final String OPENING_TIME = "1020";
	private static final String CLOSING_TIME = "2010";
	
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
	private static final String XPATH_ARTE_PREFERENCE_CB = "//*[@id=\"Arte\"]";
	private static final String XPATH_RELAX_PREFERENCE_CB = "//*[@id=\"Adrenalina\"]";
	private static final String XPATH_MUSICA_PREFERENCE_CB = "//*[@id=\"Musica\"]";
	private static final String XPATH_ADRENALINA_PREFERENCE_CB = "//*[@id=\"Arte\"]";
	private static final String XPATH_CONTINUE_BTN = "/html/body/div[1]/div[2]/form/div[13]/button";
	
	
	private static final String DRIVER_PATH = "webdriver.chrome.driver";
	private static final String DRIVER_PROPERTY = "Drivers/chromedriver";
	
	
	@Test
	public void test() {
		
		System.setProperty(DRIVER_PROPERTY, DRIVER_PATH);

		driver = new ChromeDriver();

		driver.get(LOGIN_URL);
		
		//Automatically logs in as an Explorer.
		driver.findElement(By.xpath(XPATH_USERNAME_TF)).sendKeys(USERNAME);
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
		driver.findElement(By.xpath(XPATH_ACTIVITYNAME_TF)).sendKeys(ACTIVITY_NAME);
		driver.findElement(By.xpath(XPATH_ACTIVITYDESCRIPTION_TF)).sendKeys(ACTIVITY_DESCRIPTION);
		
		driver.findElement(By.xpath(XPATH_OPENINGTIME)).sendKeys(OPENING_TIME);
		driver.findElement(By.xpath(XPATH_CLOSINGTIME)).sendKeys(CLOSING_TIME);
		
		driver.findElement(By.xpath(XPATH_ARTE_PREFERENCE_CB)).click();
		driver.findElement(By.xpath(XPATH_RELAX_PREFERENCE_CB)).click();
		driver.findElement(By.xpath(XPATH_MUSICA_PREFERENCE_CB)).click();
		driver.findElement(By.xpath(XPATH_ADRENALINA_PREFERENCE_CB)).click();
		
		driver.findElement(By.xpath(XPATH_CONTINUE_BTN)).click();
		assertEquals(true, ExpectedConditions.alertIsPresent() != null);
	}
	
	
	
}
