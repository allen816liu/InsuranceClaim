package InsuranceQuery;

import insuranceclaim.Patient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OfficeAlly {

    private static final Logger logger = LogManager.getLogger("OfficeAlly");
    private WebDriver driver;
    private WebDriverWait wait;

    private boolean loginState = false;

    public static final String uhc_portal = "https://www.uhcprovider.com/";

    public OfficeAlly() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 30);
    }


    public void login(String userName, String passWord) {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //driver.get("https://www.officeally.com/");

        driver.get("https://www.officeally.com/sLogin.aspx");

        // Maximize Window
        // driver.manage().window().maximize();

        WebElement userNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        userNameElement.clear();
        userNameElement.sendKeys(userName);

        //WebElement passwordElement = driver.findElement(By.id("Login1_Password"));
        WebElement passwordElement = driver.findElement(By.id("password"));

        // Enter something to search for
        passwordElement.clear();
        passwordElement.sendKeys(passWord);

        //WebElement loginButton = driver.findElement(By.id("Login1_LoginButton"));
        WebElement loginButton = driver.findElement(By.xpath("/html/body/main/section/div/div/div/form/div[2]/button"));


        String MainWindow = driver.getWindowHandle();
        //System.out.println("Main Windows: " + MainWindow);

        loginButton.click();
        waitForLoad(driver);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

    public void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }


    public void addPatient(String firstName, String lastName, String memberId) {

    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
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

    public List<Patient> retrievePatientDataFromOfficeAlly(List<Patient> patients) {
        waitForLoad(driver);

        retryingFindClick(By.id("SwitchCellOnlineEntry"));

        waitForLoad(driver);

        retryingFindClick(By.linkText("Professional (CMS-1500) Manage Stored Info"));

        waitForLoad(driver);

        driver.switchTo().defaultContent();

        driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Iframe9"))));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ddlPayer")));

        for (Patient patient : patients) {
            String MainWindow = driver.getWindowHandle();
            WebElement btnPatientElement = driver.findElement(By.id("btnPatient"));
            btnPatientElement.click();

            Set<String> windows = driver.getWindowHandles();

            Iterator<String> iterator = windows.iterator();

            while (iterator.hasNext()) {
                String ChildWindow = iterator.next();
                if (!MainWindow.equalsIgnoreCase(ChildWindow)) {
                    driver.switchTo().window(ChildWindow);

                    // Switching to Child window
                    boolean found = false;
                    WebElement ctl03_popupBase_txtSearchElement = driver
                            .findElement(By.id("ctl03_popupBase_txtSearch"));

                    ctl03_popupBase_txtSearchElement.clear();
                    ctl03_popupBase_txtSearchElement.sendKeys(String.valueOf(patient.getLastName()));

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
                                if (firstName.equalsIgnoreCase(patient.getFirstName())) {
                                    if (patient.getDOB() == null) {
                                        String DOB = columns.get(3).getText();
                                        String gender = columns.get(4).getText();
                                        patient.setDOB(DOB);
                                        patient.setGender(gender);
                                        found = true;
                                        columns.get(0).click();
                                        break;
                                    } else {
                                        String birthDay = columns.get(3).getText().trim();
                                        if (birthDay.equalsIgnoreCase(patient.getDOB())) {
                                            String DOB = columns.get(3).getText();
                                            String gender = columns.get(4).getText();
                                            patient.setDOB(DOB);
                                            patient.setGender(gender);
                                            found = true;
                                            columns.get(0).click();
                                            break;
                                        }
                                    }
                                }
                            }
                            if (i == 11) {
                                break;
                            }
                        }
                        if (!found && count < pages) {
                            rows.get(rows_count - 1).findElements(By.tagName("td")).get(count).click();
                            Table = driver.findElement(By.xpath("//*[@id=\"ctl03_popupBase_grvPopup\"]"));
                            rows = Table.findElements(By.tagName("tr"));
                            rows_count = rows.size();
                            //System.out.println("rows_count again = " + rows_count);
                        }
                        count++;
                    } while (!found && count <= pages);

                    driver.switchTo().window(MainWindow);
                    driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Iframe9"))));
                    WebElement editElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"Table1\"]/tbody/tr[4]/td[3]")));
                    editElement.click();
                    //WebElement updateElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnUpdatePatient")));
                    WebElement cancelElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnCancelPatient")));
                    WebElement memberIdElement = driver.findElement(By.id("PRI_PATIENT_ID"));
                    WebElement addressElement = driver.findElement(By.id("PRI_INSRD_ADDR"));
                    WebElement cityElement = driver.findElement(By.id("PRI_INSRD_CITY"));
                    WebElement zipElement = driver.findElement(By.id("PRI_INSRD_ZIP"));
                    WebElement insuranceCompanyElement = driver.findElement(By.id("PRI_INSRNCE_NAME"));
                    WebElement employerElement = driver.findElement(By.id("PRI_EMPLOYER_NAME"));
                    patient.setZipCode(zipElement.getAttribute("value"));
                    patient.setAddress1(addressElement.getAttribute("value"));
                    patient.setInsuranceID(memberIdElement.getAttribute("value"));
                    patient.setCity(cityElement.getAttribute("value"));
                    patient.setInsuranceCompany(insuranceCompanyElement.getAttribute("value"));
                    patient.setEmployer(employerElement.getAttribute("value"));
                    cancelElement.click();
                }
                // driver.switchTo().window(MainWindow);
            }
        }
        driver.close();

        return patients;
    }


    public List<Patient> getPatientsToRemoving(String csvFile) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        List<Patient> patients = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (!header) {
                    String[] dataArray = line.split(cvsSplitBy, -1);
                    Patient patient;
                    //data[1] is first name, may have birthday
                    int index = dataArray[1].indexOf("(");
                    if (index == -1) {
                        patient = new Patient(dataArray[1], dataArray[0]);
                    } else {
                        String firstName = dataArray[1].substring(0, index);
                        String birthDay = dataArray[1].substring(index + 1, dataArray[1].length() - 1);
                        patient = new Patient(firstName, dataArray[0]);
                        patient.setDOB(birthDay);
                    }
                    patients.add(patient);
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
        return patients;
    }


    /**
     * this method will create a record of a patient to officeally
     */
    public void addPatient(List<Patient> patients) {
        waitForLoad(driver);

        retryingFindClick(By.id("SwitchCellOnlineEntry"));

        waitForLoad(driver);

        retryingFindClick(By.linkText("Professional (CMS-1500) Manage Stored Info"));

        waitForLoad(driver);
        driver.switchTo().defaultContent();
        driver.switchTo().frame(wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Iframe9"))));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ddlPayer")));

        for (Patient patient : patients) {
            logger.info("add new patient {}", patient);
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
            String month = birthDayElements[0].length() < 2 ? "0" + birthDayElements[0] : birthDayElements[0];
            String day = birthDayElements[1].length() < 2 ? "0" + birthDayElements[1] : birthDayElements[1];
            String year = birthDayElements[2].length() < 4 ? "20" + birthDayElements[2] : birthDayElements[2];

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

            WebElement PRI_EMPLOYER_NAME = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("PRI_EMPLOYER_NAME")));
            PRI_EMPLOYER_NAME.clear();
            PRI_EMPLOYER_NAME.sendKeys(patient.getEmployer());

            WebElement btnUpdatePatient = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnUpdatePatient")));
            btnUpdatePatient.click();

//            WebElement cancelElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnCancelPatient")));
//            cancelElement.click();
        }
    }

    public List<Patient> getPatientsToAdd(String csvFile) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        List<Patient> patients = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(csvFile));
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (!header) {
                    line = line.replaceAll(",\\s*,", ",");
                    line = line.replaceAll("\\s*,", ",");
                    line = line.replaceAll("\\s+", " ");
                    String[] arr = line.split(cvsSplitBy, -1);
                    Patient patient = new Patient(arr[1], arr[2], arr[5], arr[4]);
                    patient.setInsuranceCompany(arr[0]);
                    patient.setInsuranceID(arr[3]);
                    patient.setAddress1(arr[6]);
                    patient.setCity(arr[7]);
                    patient.setZipCode(arr[9]);
                    if (Strings.isEmpty(arr[8])) {
                        patient.setState("CA");
                    } else {
                        patient.setState(arr[8]);
                    }
                    patient.setEmployer(arr[10]);
                    patients.add(patient);
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
        return patients;
    }

    public static void main(String[] args) {
//        OfficeAlly obj = new OfficeAlly();
//        List<Patient> patients = obj.getPatientsToRemoving("src/main/resources/patient_to_move.csv");
//        obj.login("yangli", "Oceanview118$");
//        obj.retrievePatientDataFromOfficeAlly(patients);
//        obj = new OfficeAlly(); //driver closed, so generate again
//        obj.login("xxie", "Mountain158$");
//        obj.addPatient(patients);


        OfficeAlly obj = new OfficeAlly();
        List<Patient> patients = obj.getPatientsToAdd("/Users/allenliu/Downloads/new_patient.csv");
        //obj.login("yangli", "Oceanview118$");
        obj.login("xxie", "Mountain168$");
        //obj.login("hjiang", "Mountain194$");
        obj.addPatient(patients);
    }
}
