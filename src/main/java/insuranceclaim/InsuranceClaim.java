package insuranceclaim;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.exec.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InsuranceClaim {

	private static final Logger logger = LogManager.getLogger("InsuranceClaim");

	String ddlBillingProviderName;
	int ddlBillingProviderNameSeqNumber;
	String ddlRenderingProvider;
	String ddlFacilityName;
	WebDriverWait wait;

	private WebDriver driver;

	public InsuranceClaim(String ddlBillingProviderName, String ddlRenderingProvider, String ddlFacilityName,
			int ddlBillingProviderNameSeqNumber) {
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("ignore-certificate-errors");
//		options.addArguments("start-maximized");
//		options.addArguments("enable-automation");
//		options.addArguments("--headless");
//		options.addArguments("--no-sandbox");
//		options.addArguments("--disable-infobars");
//		options.addArguments("--disable-dev-shm-usage");
//		options.addArguments("--disable-browser-side-navigation");
//		options.addArguments("--disable-gpu");
//		driver = new ChromeDriver(options);
		driver = new ChromeDriver();
		this.ddlBillingProviderName = ddlBillingProviderName.toLowerCase();
		this.ddlRenderingProvider = ddlRenderingProvider.toLowerCase();
		this.ddlFacilityName = ddlFacilityName.toLowerCase();
		this.ddlBillingProviderNameSeqNumber = ddlBillingProviderNameSeqNumber;
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

	public boolean retryingFindClick(By by) {
		boolean result = false;
		WebDriverWait wait = new WebDriverWait(driver, 10);
		int attempts = 0;
		while (attempts < 2) {
			try {
				WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
				element.click();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
			}
			attempts++;
		}
		return result;
	}

	public void addPatient(Patient patient) {
		waitForLoad(driver);

		retryingFindClick(By.id("SwitchCellOnlineEntry"));

		waitForLoad(driver);

		retryingFindClick(By.linkText("Professional (CMS-1500) Manage Stored Info"));

		waitForLoad(driver);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Iframe9"))));

		WebElement addPatientButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"Table1\"]/tbody/tr[4]/td[5]")));
		addPatientButton.click();
		WebElement firstName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("FIRST_NAME")));
		firstName.clear();
		firstName.sendKeys(patient.getFirstName());

		WebElement lastName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("LAST_NAME")));
		lastName.clear();
		lastName.sendKeys(patient.getLastName());

		WebElement PRI_INSRD_FST_NAME = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRD_FST_NAME")));
		PRI_INSRD_FST_NAME.clear();
		PRI_INSRD_FST_NAME.sendKeys(patient.getFirstName());

		WebElement PRI_INSRD_LST_NAME = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRD_LST_NAME")));
		PRI_INSRD_LST_NAME.clear();
		PRI_INSRD_LST_NAME.sendKeys(patient.getLastName());

		WebElement PRI_PATIENT_ID = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_PATIENT_ID")));
		PRI_PATIENT_ID.clear();
		PRI_PATIENT_ID.sendKeys(patient.getInsuranceID());

		WebElement INSURANCETYPE5 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("INSURANCETYPE5")));
		INSURANCETYPE5.click();

		WebElement PNT_REL_TO_INSRD_S = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PNT_REL_TO_INSRD_S")));
		PNT_REL_TO_INSRD_S.click();

		WebElement PNT_COND_FRM_EMPL_N = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PNT_COND_FRM_EMPL_N")));
		PNT_COND_FRM_EMPL_N.click();

		WebElement PNT_COND_FRM_AUTO_N = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PNT_COND_FRM_AUTO_N")));
		PNT_COND_FRM_AUTO_N.click();

		WebElement PNT_COND_FRM_OTR_N = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PNT_COND_FRM_OTR_N")));
		PNT_COND_FRM_OTR_N.click();

		WebElement PRI_INSRNCE_NAME = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRNCE_NAME")));
		PRI_INSRNCE_NAME.clear();
		PRI_INSRNCE_NAME.sendKeys(patient.getInsuranceCompany());

		String birthDay = patient.getDOB();
		String[] birthDayElements = birthDay.split("/");
		String month = birthDayElements[0].length() < 2? "0" + birthDayElements[0] : birthDayElements[0];
		String day = birthDayElements[1].length() < 2? "0" + birthDayElements[1] : birthDayElements[1];
		String year = birthDayElements[2].length() < 4? "20" + birthDayElements[2] : birthDayElements[2];

		WebElement DATE_OF_BIRTH_Month = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("DATE_OF_BIRTH_Month")));
		DATE_OF_BIRTH_Month.clear();
		DATE_OF_BIRTH_Month.sendKeys(month);

		WebElement DATE_OF_BIRTH_Day = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("DATE_OF_BIRTH_Day")));
		DATE_OF_BIRTH_Day.clear();
		DATE_OF_BIRTH_Day.sendKeys(day);

		WebElement DATE_OF_BIRTH_Year = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("DATE_OF_BIRTH_Year")));
		DATE_OF_BIRTH_Year.clear();
		DATE_OF_BIRTH_Year.sendKeys(year);

		WebElement PRI_INSRD_DOB_Month = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRD_DOB_Month")));
		PRI_INSRD_DOB_Month.clear();
		PRI_INSRD_DOB_Month.sendKeys(month);

		WebElement PRI_INSRD_DOB_Day = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRD_DOB_Day")));
		PRI_INSRD_DOB_Day.clear();
		PRI_INSRD_DOB_Day.sendKeys(day);

		WebElement PRI_INSRD_DOB_Year = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRD_DOB_Year")));
		PRI_INSRD_DOB_Year.clear();
		PRI_INSRD_DOB_Year.sendKeys(year);

		WebElement GENDER = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("GENDER")));
		Select ddlGenderSelect = new Select(GENDER);

		List<WebElement> genders = ddlGenderSelect.getOptions();

		for (WebElement gender : genders) {
			String genderName = gender.getText().toLowerCase();

			if (genderName.contains(patient.getGender().toLowerCase())) {
				gender.click();
				break;
			}
		}

		WebElement PRI_INSRD_GENDER = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRD_GENDER")));
		Select ddl_PRI_INSRD_GENDER_Select = new Select(PRI_INSRD_GENDER);
		List<WebElement> pri_genders = ddl_PRI_INSRD_GENDER_Select.getOptions();
		for (WebElement gender : pri_genders) {
			String genderName = gender.getText().toLowerCase();
			if (genderName.contains(patient.getGender().toLowerCase())) {
				gender.click();
				break;
			}
		}
		//input address
		WebElement STREET_ADDR = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("STREET_ADDR")));
		STREET_ADDR.clear();
		STREET_ADDR.sendKeys(patient.getAddress1());

		WebElement CITY = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("CITY")));
		CITY.clear();
		CITY.sendKeys(patient.getCity());

		WebElement ptSTATE = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptSTATE")));
		Select state_Select = new Select(ptSTATE);
		List<WebElement> states = state_Select.getOptions();
		for (WebElement state : states) {
			String stateName = state.getText().toLowerCase();
			if (stateName.contains(patient.getState().toLowerCase())) {
				state.click();
				break;
			}
		}

		WebElement PRI_INSRD_ADDR = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRD_ADDR")));
		PRI_INSRD_ADDR.clear();
		PRI_INSRD_ADDR.sendKeys(patient.getAddress1());

		WebElement PRI_INSRD_CITY = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRD_CITY")));
		PRI_INSRD_CITY.clear();
		PRI_INSRD_CITY.sendKeys(patient.getCity());

		WebElement ZIP = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ZIP")));
		ZIP.clear();
		ZIP.sendKeys(patient.getZipCode());

		WebElement PRI_INSRD_ZIP = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRD_ZIP")));
		PRI_INSRD_ZIP.clear();
		PRI_INSRD_ZIP.sendKeys(patient.getZipCode());

		WebElement ptPRI_INSRD_STATE = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ptPRI_INSRD_STATE")));
		Select pri_state_Select = new Select(ptPRI_INSRD_STATE);
		List<WebElement> pri_states = pri_state_Select.getOptions();
		for (WebElement state : pri_states) {
			String stateName = state.getText().toLowerCase();
			if (stateName.contains(patient.getState().toLowerCase())) {
				state.click();
				break;
			}
		}

		WebElement PRI_INSRD_HAS_2ND_N = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_INSRD_HAS_2ND_N")));
		PRI_INSRD_HAS_2ND_N.click();

		WebElement btnUpdatePatient = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnUpdatePatient")));
		btnUpdatePatient.click();
	}


	public void claim(ClaimData claimData) {

		waitForLoad(driver);

		retryingFindClick(By.id("SwitchCellOnlineEntry"));

		waitForLoad(driver);

		retryingFindClick(By.linkText("Professional (CMS-1500) Manage Stored Info"));

		waitForLoad(driver);
		driver.switchTo().defaultContent();
		driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Iframe9"))));

		WebElement ddlPayer = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ddlPayer")));
		Select ddlPayerSelect = new Select(ddlPayer);

		List<WebElement> payers = ddlPayerSelect.getOptions();

		boolean foundPayer = false;
		String patientInsuranceCompanyName = claimData.getInsuranceCompany().toLowerCase();
		for (WebElement payer : payers) {
			String payerName = payer.getText().toLowerCase();
			if (payerName.contains(patientInsuranceCompanyName)) {
				payer.click();
				foundPayer = true;
				break;
			}
		}

		if (!foundPayer) {
			logger.error("Cannot find the payer: {}", patientInsuranceCompanyName);
			throw new RuntimeException(String.format("Cannot find the payer: %s", patientInsuranceCompanyName));
		}

		String MainWindow = driver.getWindowHandle();

		WebElement btnPatientElement = driver.findElement(By.id("btnPatient"));
		btnPatientElement.click();

		Set<String> windows = driver.getWindowHandles();
		Iterator<String> iterator = windows.iterator();
		boolean foundPatient = false;

		while (iterator.hasNext()) {
			String ChildWindow = iterator.next();

			if (!MainWindow.equalsIgnoreCase(ChildWindow)) {
				// Switching to Child window
				driver.switchTo().window(ChildWindow);
				WebElement ctl03_popupBase_txtSearchElement = driver.findElement(By.id("ctl03_popupBase_txtSearch"));
				ctl03_popupBase_txtSearchElement.sendKeys(claimData.getPatientLastName());
				WebElement ctl03_popupBase_btnSearchElement = driver.findElement(By.id("ctl03_popupBase_btnSearch"));
				ctl03_popupBase_btnSearchElement.click();

				WebElement Table = driver.findElement(By.xpath("//*[@id=\"ctl03_popupBase_grvPopup\"]"));
				List<WebElement> rows = Table.findElements(By.tagName("tr"));

				int rows_count = rows.size();
				int pages;
				if (rows_count <= 11) {
					pages = 1;
				} else {
					pages = rows.get(rows_count - 1).findElements(By.tagName("td")).size();
				}
				int count = 1;

				do {
					for (int i = 1; i < rows_count; i++) {
						List<WebElement> columns = rows.get(i).findElements(By.tagName("td"));

						String cellText = columns.get(2).getText().trim();
						// System.out.println("first name is [" + cellText + "]");
						if (cellText.equalsIgnoreCase(claimData.getPatientFirstName())) {
							if (claimData.getDOB() == null) {
								foundPatient = true;
								columns.get(0).click();
								break;
							} else {
								String birthDay = columns.get(3).getText().trim();
								if (birthDay.equalsIgnoreCase(claimData.getDOB())) {
									foundPatient = true;
									columns.get(0).click();
									break;
								}
							}
						}
						if (i == 11) {
							break;
						}
					}
					if (!foundPatient && count < pages) {
						rows.get(rows_count - 1).findElements(By.tagName("td")).get(count).click();
						Table = driver.findElement(By.xpath("//*[@id=\"ctl03_popupBase_grvPopup\"]"));
						rows = Table.findElements(By.tagName("tr"));
						rows_count = rows.size();
					}
					count++;
				} while (count <= pages && !foundPatient);
			}
		}

		if (!foundPatient) {
			logger.error("Cannot find the patient with firstName: {}, lastName: {}", claimData.getPatientFirstName(),
					claimData.getPatientLastName());
			throw new RuntimeException(String.format("Cannot find the patient with firstName: %s, lastName: %s", claimData.getPatientFirstName(),
					claimData.getPatientLastName()));
		}

		driver.switchTo().window(MainWindow);

		driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Iframe9"))));

		WebElement ddlBillingProvider = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.id("ddlBillingProvider")));

		Select ddlBillingProviderSelect = new Select(ddlBillingProvider);
		List<WebElement> providers = ddlBillingProviderSelect.getOptions();

		int counter = 0;
		boolean foundBillingProvider = false;
		String _ddlBillingProviderName = claimData.getClinicName() != null? claimData.getClinicName().toLowerCase() : ddlBillingProviderName;
		for (WebElement provider : providers) {
			if (provider.getText().toLowerCase().contains(_ddlBillingProviderName)) {
				counter++;
				if (counter == ddlBillingProviderNameSeqNumber) {
					provider.click();
					foundBillingProvider = true;
					break;
				}
			}
		}
		if (!foundBillingProvider) {
			logger.error("Cannot find the billing provider: {}", _ddlBillingProviderName);
			throw new RuntimeException(String.format("Cannot find the billing provider: %s", _ddlBillingProviderName));
		}
		
		WebElement ddlRendingProvider = driver.findElement(By.id("ddlRenderingProvider"));
		Select ddlRendingProviderSelect = new Select(ddlRendingProvider);

		List<WebElement> rendingProviders = ddlRendingProviderSelect.getOptions();
		
		String _ddlRenderingProvider = claimData.getDoctorName() != null? claimData.getDoctorName().toLowerCase() : ddlRenderingProvider;

		boolean foundRendingProvider = false;
		for (WebElement rendingProvider : rendingProviders) {
			//System.out.println("rendingProvider: [" + rendingProvider.getText().toLowerCase() + "]");
			if (rendingProvider.getText().toLowerCase().contains(_ddlRenderingProvider)) {
				rendingProvider.click();
				foundRendingProvider = true;
				break;
			}
		}

		if (!foundRendingProvider) {
			logger.error("Cannot find the rending provider: {}", _ddlBillingProviderName);
			throw new RuntimeException(String.format("Cannot find the rending provider: %s", _ddlBillingProviderName));
		}

		if (!patientInsuranceCompanyName.startsWith("cigna")) {
			WebElement ddlFacility = driver.findElement(By.id("ddlFacility"));
			Select ddlFacilitySelect = new Select(ddlFacility);
	
			List<WebElement> facilitys = ddlFacilitySelect.getOptions();
			
			String _ddlFacilityName = claimData.getFacilityName() != null? claimData.getFacilityName().toLowerCase() : ddlFacilityName;
			if (_ddlFacilityName.length() > 30) {
				_ddlFacilityName = _ddlFacilityName.substring(0, 30);
			}
			
			for (WebElement facility : facilitys) {
				if (facility.getText().toLowerCase().contains(_ddlFacilityName)) {
					facility.click();
					break;
				}
			}
		}
		// ddlFacilitySelect.selectByVisibleText(ddlFacilityName);

		WebElement ddlTemplate = driver.findElement(By.id("ddlTemplate"));
		Select ddlTemplateSelect = new Select(ddlTemplate);

		List<WebElement> ddlTemplates = ddlTemplateSelect.getOptions();

		String[] visitDayData = claimData.getVisitDay().split("/");
		int year;
		int day = Integer.parseInt(visitDayData[1]);
		int month = Integer.parseInt(visitDayData[0]);
		if (visitDayData[2].length() == 2) {
			year = Integer.parseInt("20" + visitDayData[2]);
		} else {
			year = Integer.parseInt(visitDayData[2]);
		}

		String ddlTemplateName;
		if (!claimData.getTemplate().isEmpty()) {
			ddlTemplateName = claimData.getTemplate();
		} else {
			// Decide which template to select
			ddlTemplateName = "default";
			String ddlTemplateNameBase = "A+M";
			String ddlTemplateNameYear = "2019 ";

//			if (year == 2018) {
//				ddlTemplateNameYear = "";
//			}

			if (claimData.getInsuranceCompany().toLowerCase().contains("blue shield")) {
				ddlTemplateNameBase = "BlueShield";
			}

			switch (claimData.getVisitSeqNumber()) {
			case "1":
				ddlTemplateName = ddlTemplateNameYear + ddlTemplateNameBase + " 1st";
				break;
			case "2":
				ddlTemplateName = ddlTemplateNameYear + ddlTemplateNameBase + " 2nd";
				break;
			case "4":
				ddlTemplateName = ddlTemplateNameYear + ddlTemplateNameBase + " 4th";
				break;
			default:
				logger.error("wrong visit sequence number");
				break;
			}
		}

		//System.out.println("template name: " + ddlTemplateName);
		// ddlTemplateName = ddlTemplateName.replace("2018", "2019");
		// System.out.println("template name: " + ddlTemplateName);
		boolean foundTemplate = false;
		for (WebElement template : ddlTemplates) {
			//System.out.println("template option: [" + template.getText().toLowerCase() + "]");
			if (template.getText().toLowerCase().contains(ddlTemplateName.toLowerCase())) {
				template.click();
				foundTemplate = true;
				break;
			}
		}
		if (!foundTemplate) {
			logger.error("Cannot find the template: {}", ddlTemplateName);
			throw new RuntimeException(String.format("Cannot find the template: %s", ddlTemplateName));
		}
		// ddlTemplateSelect.selectByVisibleText(ddlTemplateName);

		WebElement createNewClaim = driver.findElement(By.id("Button2"));
		createNewClaim.click();

		driver.switchTo().defaultContent();
		WebElement frame9 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Iframe9")));
		driver.switchTo().frame(frame9);

		WebElement elementA = driver
				.findElement(By.xpath(".//*[@id='ctl00_phFolderContent_ucHCFA_DIAGNOSIS_CODECMS0212_1']"));
		elementA.clear();
		elementA.sendKeys(claimData.getCode());

		WebElement outSideLab = driver.findElement(By.xpath(".//*[@id='ctl00_phFolderContent_ucHCFA_OUTSIDE_LAB2']"));
		outSideLab.click();

		String dateIdTemplate = "ctl00_phFolderContent_ucHCFA_ucHCFALineItem_ucClaimLineItem_FM_DATE_OF_SVC_%s%s";
//		for (int i = 0; i < 5; i++) {
//			String monthStringId = String.format(dateIdTemplate, "MONTH", i);
//			String yearStringId = String.format(dateIdTemplate, "YEAR", i);
//			String dayStringId = String.format(dateIdTemplate, "DAY", i);
//			WebElement monthElement = driver.findElement(By.xpath(".//*[@id='" + monthStringId + "']"));
//			monthElement.sendKeys("" + month);
//			WebElement dayElement = driver.findElement(By.xpath(".//*[@id='" + dayStringId + "']"));
//			dayElement.sendKeys("" + day);
//			WebElement yearElement = driver.findElement(By.xpath(".//*[@id='" + yearStringId + "']"));
//			yearElement.sendKeys("" + year);
//			
//			if (i == 3 && !(ddlTemplateName.toLowerCase().contains("blueshield 1st")
//				|| ddlTemplateName.toLowerCase().contains("blueshield 4th"))) {
//				break;
//			}
//			//*[@id="ctl00_phFolderContent_ucHCFA_ucHCFALineItem_ucClaimLineItem_PLACE_OF_SVC3"]
////			if (i == 3 && !(ddlTemplateName.toLowerCase().contains("blueshield 1st")
////					|| ddlTemplateName.toLowerCase().contains("blueshield 4th"))) {
////					break;
////				}
//		}

		int index = 0;
		String referenceDateIdTemplate = "ctl00_phFolderContent_ucHCFA_ucHCFALineItem_ucClaimLineItem_PLACE_OF_SVC%s";
		while (true) {
			String referenceElementId = String.format(referenceDateIdTemplate, index);
			// System.out.println(referenceElementId);
			WebElement referenceElement = driver.findElement(By.xpath(".//*[@id='" + referenceElementId + "']"));
			// System.out.println(referenceElement);
			String referenceElementContent = referenceElement.getAttribute("value");
			// System.out.println(referenceElementContent);
			if (referenceElementContent != null && !referenceElementContent.isEmpty()) {
				String monthStringId = String.format(dateIdTemplate, "MONTH", index);
				String yearStringId = String.format(dateIdTemplate, "YEAR", index);
				String dayStringId = String.format(dateIdTemplate, "DAY", index);
				WebElement monthElement = driver.findElement(By.xpath(".//*[@id='" + monthStringId + "']"));
				monthElement.sendKeys("" + month);
				WebElement dayElement = driver.findElement(By.xpath(".//*[@id='" + dayStringId + "']"));
				dayElement.sendKeys("" + day);
				WebElement yearElement = driver.findElement(By.xpath(".//*[@id='" + yearStringId + "']"));
				yearElement.sendKeys("" + year);
			} else {
				break;
			}
			index++;
		}

		WebElement updateButton = driver.findElement(By.id("ctl00_phFolderContent_ucHCFA_btnSCUpdate"));
		updateButton.click();
		logger.info("Finished insurance claim for {}", claimData);
	}

	public void login(String userName, String passWord) {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//driver.get("https://www.officeally.com/");
		
		driver.get("https://www.officeally.com/sLogin.aspx");

		// Maximize Window
		// driver.manage().window().maximize();

//		WebElement loginElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav_Login")));
//		loginElement.click();

		// Enter something to search for
		//WebElement userNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Login1_UserName")));
		WebElement userNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
		userNameElement.clear();
		userNameElement.sendKeys(userName);

		//WebElement passwordElement = driver.findElement(By.id("Login1_Password"));
		WebElement passwordElement = driver.findElement(By.id("password"));

		// Enter something to search for
		passwordElement.clear();
		passwordElement.sendKeys(passWord);

		//WebElement loginButton = driver.findElement(By.id("Login1_LoginButton"));
		//WebElement loginButton = driver.findElement(By.xpath("/html/body/main/section/div/div/div/form/div[3]/button"));
		WebElement loginButton = driver.findElement(By.name("action"));

		String MainWindow = driver.getWindowHandle();
		//System.out.println("Main Windows: " + MainWindow);

		loginButton.click();
		waitForLoad(driver);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		Set<String> windows = driver.getWindowHandles();
//		Iterator<String> iterator = windows.iterator();
//		while (iterator.hasNext()) {
//			String ChildWindow = iterator.next();
//			System.out.println("ChildWindow: " + ChildWindow);
//		}

		int size = driver.findElements(By.tagName("iframe")).size();
		int index = 1;
		if (size >= 2) {
			driver.switchTo().frame(index);
			try {
				WebElement messageNumber = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lblnPopup")));
				String messageNumberStr = messageNumber.getText();
				if (messageNumberStr != null && !messageNumberStr.isEmpty()) {
					String[] messages = messageNumberStr.split("/");
					int total = Integer.valueOf(messages[1].trim());
					int i = 0;
					while (i < total) {
						String expectedValue = String.format("%s / %s", i + 1, total);
						//System.out.println("expected: " + expectedValue);
						
						messageNumber = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("lblnPopup")));
						//System.out.println("actual: " + messageNumber.getText());
						wait.until(ExpectedConditions.textToBePresentInElement(messageNumber, expectedValue));
						WebElement butKnowledgeElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"btnAcknowledge\"]")));
						butKnowledgeElement.click();
						try {
							Thread.sleep(1000);
						}catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"btnAcknowledge\"]")));
						i++;
					}
				}
			} catch (NoSuchElementException ex) {
				System.out.println(ex);
			} 
			driver.switchTo().defaultContent();
		}
		System.out.println("login successfully");
	}

	public void cleanUp() {
		driver.close();
	}

	public List<Patient> getPatientInfo(char lastNameFirstCharStart, char lastNameFirstCharEnd) {

		waitForLoad(driver);

		retryingFindClick(By.id("SwitchCellOnlineEntry"));

		waitForLoad(driver);

		retryingFindClick(By.linkText("Professional (CMS-1500) Manage Stored Info"));

		List<Patient> patients = new ArrayList<>();

		waitForLoad(driver);

		driver.switchTo().defaultContent();

		driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Iframe9"))));

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ddlPayer")));

		String MainWindow = driver.getWindowHandle();

		WebElement btnPatientElement = driver.findElement(By.id("btnPatient"));
		btnPatientElement.click();

		Set<String> windows = driver.getWindowHandles();

		Iterator<String> iterator = windows.iterator();

		while (iterator.hasNext()) {
			String ChildWindow = iterator.next();

			if (!MainWindow.equalsIgnoreCase(ChildWindow)) {
				for (char c = lastNameFirstCharStart; c <= lastNameFirstCharEnd; c++) {
					//System.out.println("lastname start with: " + c);
					// Switching to Child window
					driver.switchTo().window(ChildWindow);
					WebElement ctl03_popupBase_txtSearchElement = driver
							.findElement(By.id("ctl03_popupBase_txtSearch"));

					ctl03_popupBase_txtSearchElement.clear();
					ctl03_popupBase_txtSearchElement.sendKeys(String.valueOf(c));

					WebElement ctl03_popupBase_btnSearchElement = driver
							.findElement(By.id("ctl03_popupBase_btnSearch"));
					ctl03_popupBase_btnSearchElement.click();

					WebElement Table = driver.findElement(By.xpath("//*[@id=\"ctl03_popupBase_grvPopup\"]"));
					List<WebElement> rows = Table.findElements(By.tagName("tr"));

					//System.out.println("row size = " + rows.size());

					int rows_count = rows.size();
					int pages;
					if (rows_count <= 11) {
						pages = 1;
					} else {
						pages = rows.get(rows_count - 1).findElements(By.tagName("td")).size();
					}

					//System.out.println("pages = " + pages);
					int count = 1;

					do {
						for (int i = 1; i < rows_count; i++) {
							List<WebElement> columns = rows.get(i).findElements(By.tagName("td"));
							if (columns.size() == 6) {
								String firstName = columns.get(2).getText();
								String lastName = columns.get(1).getText();
								String DOB = columns.get(3).getText();
								String gender = columns.get(4).getText();

								Patient patient = new Patient(firstName, lastName, DOB, gender);
								// System.out.println("patient = " + patient);
								patients.add(patient);
							}
							if (i == rows_count - 3) {
								break;
							}
						}
						if (count < pages) {
							rows.get(rows_count - 1).findElements(By.tagName("td")).get(count).click();
							Table = driver.findElement(By.xpath("//*[@id=\"ctl03_popupBase_grvPopup\"]"));
							rows = Table.findElements(By.tagName("tr"));
							rows_count = rows.size();
							//System.out.println("rows_count again = " + rows_count);
						}
						count++;
					} while (count <= pages);
				}
				driver.close();
				// driver.switchTo().window(MainWindow);
			}
		}
		driver.switchTo().window(MainWindow);

//		for (Patient p : patients) {
//			System.out.println("," + p.getFirstName() + "," + p.getLastName() + ",,,,");
//		}
		return patients;
	}
	
	public List<Patient> getInsuranceCompanyInfo(List<Patient> patients, int userId) {
		for (int ii = 0; ii < patients.size(); ii++) {
			waitForLoad(driver);
			Patient patient = patients.get(ii);
			driver.switchTo().defaultContent();
			driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Iframe9"))));
			String MainWindow = driver.getWindowHandle();

			WebElement btnPatientElement = driver.findElement(By.id("btnPatient"));
			btnPatientElement.click();

			Set<String> windows = driver.getWindowHandles();
			Iterator<String> iterator = windows.iterator();
			boolean foundPatient = false;

			while (iterator.hasNext()) {
				String ChildWindow = iterator.next();

				if (!MainWindow.equalsIgnoreCase(ChildWindow)) {
					// Switching to Child window
					driver.switchTo().window(ChildWindow);
					WebElement ctl03_popupBase_txtSearchElement = driver
							.findElement(By.id("ctl03_popupBase_txtSearch"));
					ctl03_popupBase_txtSearchElement.sendKeys(patient.getLastName());
					WebElement ctl03_popupBase_btnSearchElement = driver
							.findElement(By.id("ctl03_popupBase_btnSearch"));
					ctl03_popupBase_btnSearchElement.click();

					wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"ctl03_popupBase_grvPopup\"]")));
					WebElement Table = driver.findElement(By.xpath("//*[@id=\"ctl03_popupBase_grvPopup\"]"));

					List<WebElement> rows = Table.findElements(By.tagName("tr"));

					int rows_count = rows.size();
					int pages;
					if (rows_count <= 11) {
						pages = 1;
					} else {
						pages = rows.get(rows_count - 1).findElements(By.tagName("td")).size();
					}
					int count = 1;

					do {
						for (int i = 1; i < rows_count; i++) {
							List<WebElement> columns = rows.get(i).findElements(By.tagName("td"));

							String cellText = columns.get(2).getText();
							if (cellText.equalsIgnoreCase(patient.getFirstName())) {
								foundPatient = true;
								columns.get(0).click();
								break;
							}
							if (i == 11) {
								break;
							}
						}
						if (!foundPatient && count < pages) {
							rows.get(rows_count - 1).findElements(By.tagName("td")).get(count).click();
							Table = driver.findElement(By.xpath("//*[@id=\"ctl03_popupBase_grvPopup\"]"));
							rows = Table.findElements(By.tagName("tr"));
							rows_count = rows.size();
						}
						count++;
					} while (count <= pages && !foundPatient);
				}
			}

			driver.switchTo().window(MainWindow);

			driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Iframe9"))));

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//table[2]/tbody/tr[2]/td/fieldset/table/tbody/tr[4]/td[3]")));
			WebElement editElement = driver
					.findElement(By.xpath(".//table[2]/tbody/tr[2]/td/fieldset/table/tbody/tr[4]/td[3]"));
			editElement.click();

			wait.until(ExpectedConditions.elementToBeClickable(By.id("btnCancelPatient")));

			WebElement companyElement = driver.findElement(By.id("PRI_INSRNCE_NAME"));
			String insuranceCompany = companyElement.getAttribute("value");

			WebElement idElement = driver.findElement(By.id("PRI_PATIENT_ID"));
			String inusranceId = idElement.getAttribute("value");
			WebElement monthElement = driver.findElement(By.id("DATE_OF_BIRTH_Month"));
			WebElement dayElement = driver.findElement(By.id("DATE_OF_BIRTH_Day"));
			WebElement yearElement = driver.findElement(By.id("DATE_OF_BIRTH_Year"));
			WebElement btnCancelElement = driver.findElement(By.id("btnCancelPatient"));
			WebElement genderElement = driver.findElement(By.id("GENDER"));
			String birthDay = monthElement.getAttribute("value") + "/" + dayElement.getAttribute("value") + "/"
					+ yearElement.getAttribute("value");
			// String birthDay = yearElement.getAttribute("value")+ "-" +
			// monthElement.getAttribute("value") + "-" + dayElement.getAttribute("value");
			patient.setDOB(birthDay);
			patient.setInsuranceCompany(insuranceCompany);
			patient.setInsuranceID(inusranceId);
			patient.setGender(genderElement.getAttribute("value"));
			// System.out.println(patient);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btnCancelElement);
			btnCancelElement.click();
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.id("btnPatient")));
		
//		StringBuilder builder = new StringBuilder();
//		builder.append("[\n");
//		for (int j = 0; j < patients.size(); j++) {
//			builder.append("{\"model\": \"billing.patient\",\"pk\": ").append(j).append(",").append("\"fields\": {");
//			builder.append("\"firstname\": ").append("\"").append(patients.get(j).getFirstName()).append("\"").append(",");
//			builder.append("\"lastname\": ").append("\"").append(patients.get(j).getLastName()).append("\"").append(",");
//			builder.append("\"insurancecompany\": ").append("\"").append(patients.get(j).getInsuranceCompany()).append("\"").append(",");
//			builder.append("\"insurancenumber\": ").append("\"").append(patients.get(j).getInsuranceID()).append("\"").append(",");
//			builder.append("\"dob\": ").append("\"").append(patients.get(j).getDOB()).append("\"").append(",");
//			builder.append("\"userid\": ").append(userId).append("}},\n");
//		}
//		builder.deleteCharAt(builder.length() - 1);
//		builder.deleteCharAt(builder.length() - 1);
//		builder.append("\n]");
//
//		allPatients(builder, patients);
//
//		System.out.println(builder.toString());

		return patients;
	}

	public void writePatientToFile(List<Patient> patients, String fileName) {
		try (FileWriter fileWriter = new FileWriter(fileName, true);
			 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			 PrintWriter printWriter = new PrintWriter(bufferedWriter);) {
			StringBuilder builder = new StringBuilder();
			allPatients(builder, patients);
			printWriter.println(builder);
		 }
		catch (IOException i) {
			i.printStackTrace();
		}
	}

	private void appendHeader(StringBuilder builder) {
		builder.append("InsuranceCompany,FirstName,LastName,Gender,DOB,InsuranceId");
		builder.append("\n");
	}

	private void allPatients(StringBuilder builder, List<Patient> patients) {
		appendHeader(builder);
		for (int j = 0; j < patients.size(); j++) {
			builder.append(patients.get(j).getInsuranceCompany()).append(",");
			builder.append(patients.get(j).getFirstName()).append(",");
			builder.append(patients.get(j).getLastName()).append(",");
			builder.append(patients.get(j).getGender()).append(",");
			builder.append(patients.get(j).getDOB()).append(",");
			builder.append(patients.get(j).getInsuranceID());
			builder.append("\n");
		}
	}
	
	private String[] extractDate(String input) {
		
		String regex = "\\(\\s*(\\d{1,2}/\\d{1,2}/\\d{4})\\s*\\)";
		Matcher m = Pattern.compile(regex).matcher(input);
		if (m.find()) {
			String[] result = new String[2];
		    result[1] = m.group(1);
		    String[] birth = result[1].split("/");
		    if (birth[0].length() < 2) {
		    	birth[0] = "0" + birth[0];
		    }
		    if (birth[1].length() < 2) {
		    	birth[1] = "0" + birth[1];
		    }
		    result[1] = String.join("/", birth);
		    result[0] = input.replace(m.group(0), "").trim();
		    return result;
		} else {
		    return null;
		}	
	}

	public List<ClaimData> getClaimData(String csvFile) {

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		Set<String> codeSet = new HashSet<>(Arrays.asList(codes));

		List<ClaimData> claimDataList = new ArrayList<>();
		try {
			
			br = new BufferedReader(new FileReader(csvFile));
			boolean header = true;
			Calendar cal = Calendar.getInstance();
			String year = cal.get(Calendar.YEAR) + "";
			String month = cal.get(Calendar.MONTH) + 1 + "";
			while ((line = br.readLine()) != null) {
				if (!header) {
					String[] dataArray = line.split(cvsSplitBy, -1);
					String date = dataArray[3];
					String[] dateArray = date.split("/");
					if (dateArray.length == 3) {
						year = dateArray[2];
						month = dateArray[0];
						dataArray[3] = dataArray[3];
					} else if (dateArray.length == 2) {
						month = dateArray[0];
						dataArray[3] = dataArray[3].trim() + "/" + year;
					} else {
						dataArray[3] = month + "/" + dataArray[3].trim() + "/" + year;
					}
					dataArray[3] = format_data(dataArray[3]);
					
					ClaimData claimData;
					validate_data(dataArray, codeSet);
					if (dataArray.length >= 10) {
						claimData = new ClaimData(dataArray[0], dataArray[1], dataArray[2], dataArray[3],
							dataArray[4], dataArray[5], dataArray[6], dataArray[7], dataArray[8], dataArray[9]);
						if (dataArray.length == 11) {
							claimData.setComment(dataArray[10]);
						}
					} else {
						claimData = new ClaimData(dataArray[0], dataArray[1], dataArray[2], dataArray[3],
								dataArray[4], dataArray[5], dataArray[6]);
					}
					
					String[] firstNameAndBirthDay = extractDate(dataArray[1]);
					if (firstNameAndBirthDay != null) {
						claimData.setPatientFirstName(firstNameAndBirthDay[0]);
						claimData.setDOB(firstNameAndBirthDay[1]);
					}
					
					claimDataList.add(claimData);
					//System.out.println(claimData);
				} else {
					header = false;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return claimDataList;
	}
	
	private String format_data(String dateStr) {
		String[] dateArray = dateStr.split("/");
		String month = dateArray[0].length() == 1? "0" + dateArray[0] : dateArray[0]; 
		String day = dateArray[1].length() == 1? "0" + dateArray[1] : dateArray[1]; 
		return month + "/" + day + "/" + dateArray[2];
	}
	
	static final String[] codes = {
			"M54.2", "M54.9","G43.909", "M54.50", "M25.511","M25.512","M25.521","M25.522","M25.531","M25.532",
			"M25.551","M25.552","M25.561","M25.562","M25.571","M25.572","M79.651","M79.652","M79.601","M79.602",
			"M79.643","M79.642","M79.641","M79.671","M79.672", "R10.819", "M25.519" };
	
	private void validate_data(String[] arr, Set<String> codeSet) {
		//ClaimData(String insuranceCompany, String patientFirstName, String patientLastName, String visitDay,
		//		String visitSeqNumber, String code, String template, String clinicName, String facilityName, String doctorName) {
		for (int i = 0; i < arr.length; i++) {
			if ("".equals(arr[i])) {
				throw new RuntimeException("empty string, data is: " + String.join(",", arr));
			}
		}
		if (!codeSet.contains(arr[5])) {
			System.out.println(String.format("[%s]", arr[5]));
			throw new RuntimeException(arr[5] + " is not a right code");
		}
		if (!validate_date(arr[3])) {
			throw new RuntimeException(arr[3] + " is not a validate date");
		}
	}
	
	private boolean validate_date(String visitDay) {
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(visitDay);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
	

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub

		// xiaoyun li
//		String ddlBillingProviderName = "Xiaoyun Li";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Li, Xiaoyun";
//		String ddlFacilityName = "Feng Ye Acupuncture Clinic";
//		String userName = "xiaoyunli";
//		String password = "Oceanview95$";
//		int userId = 1;

		// jiang hong for blue shield
//		String ddlBillingProviderName = "Hong Jiang";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Jiang, Hong";
//		String ddlFacilityName = "Hong Jiang";
//		String userName = "hjiang";
//		String password = "Mountain194$";
//		int userId = 1;

		// jiang hong for others
//		String ddlBillingProviderName = "Cedar Eastern Traditional Healthcare Inc";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Jiang, Hong";
//		String ddlFacilityName = "Cedar Eastern Traditional Heal";
//		String userName = "hjiang";
//		String password = "Mountain194$";
//		int userId = 1;

		// Yang Li
//		String ddlBillingProviderName = "Yang HealthCare Medical Group";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Li, Yang";
//		String ddlFacilityName = "Yang HealthCare Medical Group";
//		String userName = "yangli";
//		String password = "Oceanview118$";
//		int userId = 2;

//		 Yang Li Company
//		 String ddlBillingProviderName = "Acuherb Medical Group";
//		 int ddlBillingProviderNameSeqNumber = 1;
//		 String ddlRenderingProvider = "Li, Yang";
//		 String ddlFacilityName = "Acuherb Medical Group";
//		 String userName = "yangli";
//		 String password = "Oceanview118$";
//		 int userId = 2;
 
		// Jing Chen
//		String ddlBillingProviderName = "Jing-Cheng Acupuncture & Herbs";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Shang, Aping";
//		String ddlFacilityName = "Jing-Cheng Acupuncture & Herbs";
//		String userName = "xxie";
//		String password = "Mountain151$";
//		int userId = 2;
		
		// Xiangping Xie Blue Shield
//		String ddlBillingProviderName = "Xiangping Xie";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Xie, Xiangping";
//		String ddlFacilityName = "Xiangping Xie";
//		String userName = "xxie";
//		String password = "Mountain168$";
//		int userId = 2;
		
		// Xiangping Xie
		String ddlBillingProviderName = "Annie Acuhealing Clinic";
		int ddlBillingProviderNameSeqNumber = 1;
		String ddlRenderingProvider = "Xie, Xiangping";
		String ddlFacilityName = "Annie Acuhealing Clinic";
		String userName = "xxie";
		String password = "Mountain168$";
		int userId = 2;
		
		// Duan in Xiangping Xie Clinc
//		String ddlBillingProviderName = "Annie Acuhealing Clinic";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Duan, Qionglian";
//		String ddlFacilityName = "Annie Acuhealing Clinic";
//		String userName = "xxie";
//		String password = "Mountain152$";
//		int userId = 2;
//		
		// Xiaoyun Li in Xiangping Xie Clinc
//		String ddlBillingProviderName = "Annie Acuhealing Clinic";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Li, Xiaoyun";
//		String ddlFacilityName = "Annie Acuhealing Clinic";
//		String userName = "xxie";
//		String password = "Mountain151$";
//		int userId = 2;
		
		// Made 4 u
//		String ddlBillingProviderName = "Made4U Acupuncture Center";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Chai, Chsu";
//		String ddlFacilityName = "Made4U Acupuncture Center";
//		String userName = "xxie";
//		String password = "Mountain151$";
//		int userId = 2;
		
		//Duan Qionglian
//		String ddlBillingProviderName = "Lian Healing INC";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Duan, Qionglian";
//		String ddlFacilityName = "Lian Healing INC";
//		String userName = "xxie";
//		String password = "Mountain151$";
//		int userId = 2;
		
//		Amanda Company
//		String ddlBillingProviderName = "Natural Wellness Center";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Xu, Jian";
//		String ddlFacilityName = "Natural Wellness Center";
//		String userName = "xxie";
//		String password = "Mountain151$";
//		int userId = 2;
		
//		Xiaohua's clinic		
//		String ddlBillingProviderName = "Tranquility Healing Acupuncture Clinic";
//		int ddlBillingProviderNameSeqNumber = 1;
//		String ddlRenderingProvider = "Li, Xiaoyun";
//		String ddlFacilityName = "Tranquility Healing Acupunctur";
//		String userName = "xxie";
//		String password = "Mountain151$";
//		int userId = 2;
		
		// claim
		InsuranceClaim obj = new InsuranceClaim(ddlBillingProviderName, ddlRenderingProvider, ddlFacilityName,
				ddlBillingProviderNameSeqNumber);

		obj.login(userName, password);

		String fileName = "src/main/resources/claimdetail.csv";
		List<ClaimData> claimList = obj.getClaimData(fileName);
		for (ClaimData claimData : claimList) {
			if (claimData.getClinicName() == null) {
				claimData.setClinicName(ddlBillingProviderName);
			}
			if (claimData.getDoctorName() == null) {
				claimData.setDoctorName(ddlRenderingProvider);
			}
			if (claimData.getFacilityName() == null) {
				claimData.setFacilityName(ddlFacilityName);
			}
		}

		for (ClaimData claimData : claimList) {
			try {
				if (claimData.getVisitDay() != null) {
					obj.claim(claimData);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Claim {} failed, reason {}", claimData, e.getMessage());
			}
		}
		// end of claim
		
		//patient info
//		InsuranceClaim obj = new InsuranceClaim(ddlBillingProviderName, ddlRenderingProvider, ddlFacilityName,
//				ddlBillingProviderNameSeqNumber);
//		obj.login(userName, password);
//		List<Patient> patients = obj.getPatientInfo('a', 'z');
//		patients = obj.getInsuranceCompanyInfo(patients, userId);
//		obj.writePatientToFile(patients, "patientInfo.txt");
		// end of info

//		
//        // 1. Patient insurance company name comparator
//        Comparator<Patient> insuranceCompator = 
//                (p1, p2) -> p1.getInsuranceCompany().compareTo(p2.getInsuranceCompany());
// 
//        // 2. Patient first name comparator
//        Comparator<Patient> firstNameCompator = 
//                (p1, p2) -> p1.getFirstName().compareTo(p2.getFirstName());
// 
//        // 3. Patient last name comparator
//        Comparator<Patient> lastNameCompator =  
//        		(p1, p2) -> p1.getLastName().compareTo(p2.getLastName());
//        		 
//        // sorting on multiple fields (3-level) using Lambda expression
//        List<Patient> sortedPatients = patients
//                .stream()
//                .sorted(
//                		insuranceCompator // 1st compare Name
//                        .thenComparing(firstNameCompator) // then 2nd compare City
//                        .thenComparing(lastNameCompator)) // then 3rd compare Age
//                .collect(Collectors.toList()); 
//		
//		System.out.println("InsuranceCompany, FirstName, LastName, DOB");
//		for (Patient patient : sortedPatients) {
//			System.out.println(patient);
//		}
		//end patient info


//		//add patient in officeAlly
//		InsuranceClaim obj = new InsuranceClaim(ddlBillingProviderName, ddlRenderingProvider, ddlFacilityName,
//		ddlBillingProviderNameSeqNumber);
//		obj.login(userName, password);
//
//		Patient patient = new Patient("Hongjun", "Liu", "Aetna", "W251153500", "1/4/1965");
//		patient.setGender("Male");
//		patient.setAddress1("21230 Homestead Rd");
//		patient.setCity("Cupertino");
//		patient.setZipCode("95014");
//		obj.addPatient(patient);
//		//end add patient

		obj.driver.close();
	}

}
