package brokenLinkAndImageMP;

import static org.testng.Assert.assertEquals;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import dataDriven.dataDriven;
import extentReport.BaseTestMP;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BrokenImageMP extends BaseTestMP {
	public ExtentTest extentTest;

	String loginPage = "https://vnshealth-crm--fullsbx.sandbox.my.site.com/member/login";

	@BeforeTest
	public void loginMember() throws IOException, InterruptedException {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(loginPage);

		dataDriven d = new dataDriven();

		ArrayList TS05 = d.getData("TS05", "TC01");
		String username = (String) TS05.get(6);
		ArrayList TS06 = d.getData("TS06", "TC01");
		String password = (String) TS06.get(6);

		WebElement loginBtn = driver.findElement(By.xpath("//button[normalize-space()='Login']"));
		loginBtn.click();

		WebElement okBtn = driver.findElement(By.xpath("//button[normalize-space()='OK']"));
		okBtn.click();

		// enter username
		WebElement uname = driver.findElement(By.xpath("//input[@id='username']"));
		uname.sendKeys(username);

		// enter password
		WebElement pwd = driver.findElement(By.xpath("//input[@id='password']"));
		pwd.sendKeys(password);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// login
		WebElement signOn = driver.findElement(By.xpath("//button[contains(text(), 'Sign On')]"));
		signOn.click();
		Thread.sleep(10000);

		log("The Login Page url is: " + loginPage);
		log("Login is successful with user name : " + username);
		Thread.sleep(5000);
	}

	// 1. Homepage
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesHomepage() throws InterruptedException {
		Thread.sleep(5000);
		testBrokenImages();
	}

	// 2. Benefits -Benefits
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesBenefits() throws InterruptedException {
		WebElement benefitsMenu = driver.findElement(By.xpath("//div[contains(text(),'Benefits')]"));
		benefitsMenu.click();

		WebElement benefitsSubMenu = driver.findElement(
				By.xpath("//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='Benefits']"));
		benefitsSubMenu.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 3. Benefits -Pharmacy and Prescriptions
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesPharmacyAndPrescp() throws InterruptedException {
		WebElement benefitsMenu = driver.findElement(By.xpath("//div[contains(text(),'Benefits')]"));
		benefitsMenu.click();

		WebElement pharmacyMenu = driver.findElement(By.xpath(
				"//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='Pharmacy & Prescriptions']"));
		pharmacyMenu.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 4. Benefits -ID Card
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesIDCard() throws InterruptedException {
		WebElement benefitsMenu = driver.findElement(By.xpath("//div[contains(text(),'Benefits')]"));
		benefitsMenu.click();

		WebElement idCardMenu = driver
				.findElement(By.xpath("(//a[@data-type='comm__namedPage'][normalize-space()='ID Card'])[2]"));
		idCardMenu.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 5. Benefits -OTC Benefits
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesOTCBenefits() throws InterruptedException {
		WebElement benefitsMenu = driver.findElement(By.xpath("//div[contains(text(),'Benefits')]"));
		benefitsMenu.click();

		WebElement otcBenefitsMenu = driver.findElement(
				By.xpath("//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='OTC Benefits']"));
		otcBenefitsMenu.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 6. Benefits -Rewards
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesReward() throws InterruptedException {
		WebElement benefitsMenu = driver.findElement(By.xpath("//div[contains(text(),'Benefits')]"));
		benefitsMenu.click();

		WebElement rewardsMenu = driver.findElement(
				By.xpath("//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='Rewards']"));
		rewardsMenu.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 7. Benefits -Plan History
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesPlanHistory() throws InterruptedException {
		WebElement benefitsMenu = driver.findElement(By.xpath("//div[contains(text(),'Benefits')]"));
		benefitsMenu.click();

		WebElement planHostoryMenu = driver.findElement(
				By.xpath("//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='Plan History']"));
		planHostoryMenu.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 8. My Care -My PCP
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesMyPCP() throws InterruptedException {
		WebElement myCareMenu = driver.findElement(By.xpath("//div[contains(text(),'My Care')]"));
		myCareMenu.click();

		WebElement myPCPMenu = driver.findElement(
				By.xpath("//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='My PCP']"));
		myPCPMenu.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 9. My Care -My Healthplan Care Team
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesMyHealthPlanTeam() throws InterruptedException {
		WebElement myCareMenu = driver.findElement(By.xpath("//div[contains(text(),'My Care')]"));
		myCareMenu.click();

		WebElement myHealthPlanTeamMenu = driver.findElement(By.xpath(
				"//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='My Health Plan Care Team']"));
		myHealthPlanTeamMenu.click();
		Thread.sleep(10000);
		testBrokenImages();

	}

	// 10. My Care -Service Authorizations
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesServiceAuthorizations() throws InterruptedException {
		WebElement myCareMenu = driver.findElement(By.xpath("//div[contains(text(),'My Care')]"));
		myCareMenu.click();

		WebElement serviceAuth = driver.findElement(By.xpath(
				"//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='Service Authorizations']"));
		serviceAuth.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 11. My Care -My Medical Supplies and Equipment
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesMyMedicalSuppliesandEquipment() throws InterruptedException {
		WebElement myCareMenu = driver.findElement(By.xpath("//div[contains(text(),'My Care')]"));
		myCareMenu.click();

		WebElement myMedicalSuppliesandEquipment = driver.findElement(By.xpath(
				"//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='My Medical Supplies and Equipment']"));
		myMedicalSuppliesandEquipment.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 12. Claims -My Claims
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesClaims() throws InterruptedException {
		WebElement claimsMenu = driver.findElement(By.xpath("//div[contains(text(),'Claims')]"));
		claimsMenu.click();

		WebElement myClaims = driver.findElement(
				By.xpath("//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='My Claims']"));
		myClaims.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 13. Resources -My Plan Resources
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesMyPlanResources() throws InterruptedException {
		WebElement resourcesMenu = driver.findElement(By.xpath("//div[contains(text(),'Resources')]"));
		resourcesMenu.click();

		WebElement myPlanResources = driver.findElement(By.xpath(
				"//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='My Plan Resources']"));
		myPlanResources.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 14. Resources -Benefit Partners
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesBenefitPartners() throws InterruptedException {
		WebElement resourcesMenu = driver.findElement(By.xpath("//div[contains(text(),'Resources')]"));
		resourcesMenu.click();

		WebElement benefitPartners = driver.findElement(By.xpath(
				"//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='Benefit Partners']"));
		benefitPartners.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 15. Grievances & Appeals- Grievances
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesGrievances() throws InterruptedException {
		WebElement grievancesAppealsMenu = driver
				.findElement(By.xpath("//div[contains(text(),'Grievances & Appeals')]"));
		grievancesAppealsMenu.click();

		WebElement grievances = driver.findElement(
				By.xpath("//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='Grievances']"));
		grievances.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 16. Grievances & Appeals- Appeals
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesAppeals() throws InterruptedException {
		WebElement grievancesAppealsMenu = driver
				.findElement(By.xpath("//div[contains(text(),'Grievances & Appeals')]"));
		grievancesAppealsMenu.click();

		WebElement appeals = driver.findElement(
				By.xpath("//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='Appeals']"));
		appeals.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 17. Communication Center
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesComCenter() throws InterruptedException {
		WebElement comCenterMenu = driver.findElement(By.xpath("//div[contains(text(),'Communication Center')]"));
		comCenterMenu.click();

		WebElement comCenterSubMenu = driver.findElement(By.xpath(
				"//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='Communication Center']"));
		comCenterSubMenu.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	// 18. My Account
	@Test(groups = "Broken Image Test")
	public void testBrokenImagesMyAccount() throws InterruptedException {
		WebElement myAccountMenu = driver.findElement(By.xpath("//div[contains(text(),'My Account')]"));
		myAccountMenu.click();

		WebElement myAccountSubMenu = driver.findElement(
				By.xpath("//ul[@class='submenu']//a[@data-type='comm__namedPage'][normalize-space()='My Account']"));
		myAccountSubMenu.click();
		Thread.sleep(10000);
		testBrokenImages();
	}

	@AfterTest
	public void tearUp() {
		driver.close();
	}
}
