package insuranceclaim;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClaimQuery {
	private WebDriver driver;
	private WebDriverWait wait;
	
	public ClaimQuery() {
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 30);
	}
	
	public void queryClaim(Patient patient, Date start, Date end) {
		
	}
	
	public void login_availity(String userName, String password) {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("https://www.availity.com/");

		waitForLoad(driver);
		
		// Maximize Window
		driver.manage().window().maximize();
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Availity Portal'])[1]/following::button[1]")).click();
	    waitForLoad(driver);
	    driver.findElement(By.id("userId")).click();
	    driver.findElement(By.id("userId")).clear();
	    driver.findElement(By.id("userId")).sendKeys("xiaoyunli1946");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("Winter89$");
	    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Forgot your user ID?'])[1]/following::span[1]")).click();
	}
	
	public void query_availity(Patient patient, Date start, Date end) throws Exception {
	
		waitForLoad(driver);
		driver.switchTo().frame("newBodyFrame");
	    driver.findElement(By.xpath("html/body/div/dashboard/div/div[1]/top-apps/div/div[2]/div/div/a/span/div/h3")).click();
	    
	    waitForLoad(driver);
	    WebElement payers = driver.findElement(By.id("id12"));
	    
		Select payersSelect = new Select(payers);
		
		List<WebElement> payerSelects = payersSelect.getOptions();
		for (WebElement payer : payerSelects) {
			if (payer.getText().toUpperCase().contains("ANTHEM - CA")) {			
				payer.click();
				break;
			}
		}
		waitForLoad(driver);
	    driver.findElement(By.id("id59")).click();
	    driver.findElement(By.id("id59")).clear();
	    driver.findElement(By.id("id59")).sendKeys("1013350420");
	    driver.findElement(By.id("id5f")).click();
	    driver.findElement(By.id("id5f")).clear();
	    driver.findElement(By.id("id5f")).sendKeys("GGS150M85876");
	    driver.findElement(By.id("id62")).click();
	    driver.findElement(By.id("id62")).clear();
	    driver.findElement(By.id("id62")).sendKeys("SAHASRABUDHE");
	    driver.findElement(By.id("id64")).click();
	    driver.findElement(By.id("id64")).clear();
	    driver.findElement(By.id("id64")).sendKeys("SHEETAL");
	    driver.findElement(By.id("id42")).click();
	    driver.findElement(By.id("id42")).clear();
	    driver.findElement(By.id("id42")).sendKeys("03");
	    driver.findElement(By.id("id43")).clear();
	    driver.findElement(By.id("id43")).sendKeys("18");
	    driver.findElement(By.id("id44")).clear();
	    driver.findElement(By.id("id44")).sendKeys("1979");
	    driver.findElement(By.id("id67")).click();
	    new Select(driver.findElement(By.id("id67"))).selectByVisibleText("Female");
	    driver.findElement(By.id("id45")).click();
	    driver.findElement(By.id("id45")).clear();
	    driver.findElement(By.id("id45")).sendKeys("03");
	    driver.findElement(By.id("id46")).clear();
	    driver.findElement(By.id("id46")).sendKeys("23");
	    driver.findElement(By.id("id47")).clear();
	    driver.findElement(By.id("id47")).sendKeys("2019");
	    driver.findElement(By.id("id48")).click();
	    driver.findElement(By.id("id48")).clear();
	    driver.findElement(By.id("id48")).sendKeys("04");
	    driver.findElement(By.id("id49")).clear();
	    driver.findElement(By.id("id49")).sendKeys("11");
	    driver.findElement(By.id("id4a")).clear();
	    driver.findElement(By.id("id4a")).sendKeys("2019");
	    driver.findElement(By.id("id78")).click();
	}
	
	public void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(pageLoadCondition);
	}
	
//	public static void main(String[] args) {
//		ClaimQuery claimQueryIns = new ClaimQuery();
//		claimQueryIns.login_availity("", "");
//		try {
//			claimQueryIns.query_availity(null, null,  null);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println();
//	}
}
