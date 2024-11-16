package InsuranceQuery;

import insuranceclaim.Patient;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.List;
import java.util.concurrent.TimeUnit;

public class UHC_Eligibility implements Eligiility {
    private WebDriver driver;
    private WebDriverWait wait;

    private boolean loginState = false;

    public static final String uhc_portal = "https://www.uhcprovider.com/";

    public UHC_Eligibility() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 30);
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

    @Override
    public void login(String userName, String passWord)
    {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(uhc_portal);

        // Maximize Window
        // driver.manage().window().maximize();

        WebElement signInElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("signin-btn-wrapper")));
        signInElement.click();


        driver.findElement(By.linkText("Sign in to the UnitedHealthcare Provider Portal")).click();
        //WebElement portalElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"signin-btn-wrapper\"]/div/ul/li[1]/a")));
        WebElement portalElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Sign in to the UnitedHealthcare Provider Portal")));
        portalElement.click();

        WebElement userNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        userNameElement.clear();
        userNameElement.sendKeys(userName);

        WebElement continueButton = driver.findElement(By.id("btnLogin"));
        continueButton.click();

        WebElement passwordElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-pwd")));
        passwordElement.clear();
        passwordElement.sendKeys(passWord);

        continueButton = driver.findElement(By.id("btnLogin"));
        continueButton.click();

        waitForLoad(driver);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Sign Out")));
        System.out.println("login successfully");
        loginState = true;
    }

    @Override
    public Patient checkEligiility(String memberId, String firstName, String lastName) {
        WebElement eligibilityElement = driver.findElement(By.linkText("Eligibility"));
        eligibilityElement.click();

        WebElement searchCriteriaElement = driver.findElement(By.id("select-search-criteria-inputnull"));
        Select searchCriteriaSelect = new Select(searchCriteriaElement);

        List<WebElement> searchCriteriaLists = searchCriteriaSelect.getOptions();

        for (WebElement searchCriteria : searchCriteriaLists) {
            if (searchCriteria.getText().toLowerCase().contains("Member ID & Member Name")) {
                searchCriteria.click();
                break;
            }
        }

        Patient patient = new Patient(firstName, lastName, memberId);

        WebElement memberIdElement = driver.findElement(By.id("eligibility-memberid-input"));
        memberIdElement.clear();
        memberIdElement.sendKeys(patient.getInsuranceID());
        WebElement firstNameElement = driver.findElement(By.id("firstname-input"));
        firstNameElement.clear();
        firstNameElement.sendKeys(patient.getFirstName());
        WebElement lastNameElement = driver.findElement(By.id("lastname-input"));
        lastNameElement.clear();
        lastNameElement.sendKeys(patient.getLastName());
        WebElement submitElement = driver.findElement(By.id("submit-search-button"));
        submitElement.click();

        return null;
    }

    public void test() {
        driver.get("https://www.uhcprovider.com/");
        WebElement signInElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("signin-btn-wrapper")));
        signInElement.click();

        //WebElement portalElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"signin-btn-wrapper\"]/div/ul/li[1]/a")));
        WebElement portalElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Sign in to the UnitedHealthcare Provider Portal")));
        portalElement.click();

        WebElement userNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        userNameElement.clear();
        userNameElement.sendKeys("annie1025168");

        WebElement continueButton = driver.findElement(By.id("btnLogin"));
        continueButton.click();

        WebElement passwordElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-pwd")));
        passwordElement.clear();
        passwordElement.sendKeys("Sunnyvale108$");

        continueButton = driver.findElement(By.id("btnLogin"));
        continueButton.click();

        WebElement eligibilityElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Eligibility")));
        eligibilityElement.click();
        WebElement searchType = driver.findElement(By.cssSelector("div[data-testid=\"eligibility-search-type-container\"]"));
        searchType.click();
        //driver.findElement(By.cssSelector(".css-z48sl7")).click();
        driver.findElement(By.id("react-select-select-search-criteria-inputnull-option-1")).click();
        driver.findElement(By.id("eligibility-memberid-input")).click();
        driver.findElement(By.id("eligibility-memberid-input")).sendKeys("12345");
        driver.findElement(By.id("firstname-input")).click();
        driver.findElement(By.id("firstname-input")).sendKeys("allen");
        driver.findElement(By.id("lastname-input")).click();
        driver.findElement(By.id("lastname-input")).sendKeys("liu");
        driver.findElement(By.id("submit-search-button")).click();
    }

    public static void main(String[] args) {
        UHC_Eligibility obj = new UHC_Eligibility();
//        obj.login("annie1025168", "Sunnyvale108$");
//        obj.checkEligiility("Pankur", "Agarwal", "982843231");

        obj.test();

    }
}
