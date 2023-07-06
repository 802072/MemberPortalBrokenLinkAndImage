package brokenLinkAndImage;

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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import dataDriven.dataDriven;
import extentReport.BaseTest1;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.classic.methods.HttpGet;

public class BrokenLink extends BaseTest1 {
	public ExtentTest extentTest;

	String myhomePage = "https://vnshealth-crm--fullsbx.sandbox.my.site.com/member/login?ec=302&startURL=%2Fmember%2F";

	String myurl = "";
	HttpURLConnection myhuc = null;
	int responseCode = 200;

	//String excelPath = "C:\\Users\\802072\\eclipse-workspace1\\MemberPortalBrokenLinkAndImage1\\src\\test\\resources\\testData\\testData.xlsx";
	String sheetName = "loginInfo";
	WebDriver driver;

	@Parameters({ "browserName" })
	@BeforeMethod(alwaysRun = true)
	public void setup(String browserName) throws IOException, InterruptedException {
		if (browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}

		else if (browserName.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String browserName1 = cap.getBrowserName().toLowerCase();
		String v = cap.getVersion().toString();
		log("Browser Name= " + browserName1 + ", Browser Version= " + v);

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get(myhomePage);

		dataDriven d = new dataDriven();
		ArrayList data = d.getData("User2", "loginInfo");
		String username = (String) data.get(1);
		System.out.println(username);
		String password = (String) data.get(2);

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
		// Assert.assertEquals(driver.findElement(By.xpath("//a[text()='Home']")).getText(),
		// "Home");
		log("The Login Page url is: " + myhomePage);
		log("Login is successful with user name : " + data.get(1));
		Thread.sleep(5000);
	}

	@Test(groups = "Broken Link Test")
	public void testBrokenLinks() throws InterruptedException {
		ArrayList allList = new ArrayList();
		ArrayList emptyLst = new ArrayList();
		ArrayList anotherDomainLst = new ArrayList();
		ArrayList brokenLst = new ArrayList();
		ArrayList myDomainLst = new ArrayList();
		Thread.sleep(10000);
		//List<WebElement> mylinks = driver.findElements(By.xpath("//a"));
		// List<WebElement> mylinks = driver.findElements(By.tagName("a"));
		 List<WebElement> mylinks = driver.findElements(By.xpath("//a[@href]"));
		// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(5000);
		System.out.println(mylinks);
		Iterator<WebElement> myit = mylinks.iterator();
		while (myit.hasNext()) {
			myurl = myit.next().getAttribute("href");
			allList.add(myurl);
			System.out.println(myurl);
			if (myurl == null || myurl.isEmpty()) {
				// System.out.println("Empty URL or an Unconfigured URL");
				emptyLst.add(myurl);
				continue;
			}
			if (myurl.contains("https://vnshealth")) { 
				myDomainLst.add(myurl);
				continue;
			}

			if (!myurl.contains("https://vnshealth")) {
				anotherDomainLst.add(myurl);
				continue;
			}

			try {
				myhuc = (HttpURLConnection) (new URL(myurl).openConnection());
				myhuc.setRequestMethod("HEAD");
				myhuc.connect();
				responseCode = myhuc.getResponseCode();

				if (responseCode >= 400) {
					System.out.println(myurl + " This link is broken");

					brokenLst.add(myurl);
					System.out.println("The broken link response code is:" + responseCode);

				} else {
					// System.out.println(myurl + " This link is valid");
				}
				// System.out.println("The response code is:"+responseCode);
			} catch (MalformedURLException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		log("There are " + allList.size() + " urls in the page under test");
		// allList.forEach(t -> log((String) t));
		System.out.println(allList.size());
		System.out.println(allList);
		log("*******************************************************");

		log("A total of " + myDomainLst.size() + " urls in the page are from the same domain");
		log("The urls from same domain are listed below:");
		myDomainLst.forEach(t -> log((String) t));

		log("*******************************************************");
		log("A total of " + anotherDomainLst.size() + " urls in the page are from other domains");
		log("The urls from other domains are listed below:");
		anotherDomainLst.forEach(t -> log((String) t));

		log("*******************************************************");
		log("A total of " + emptyLst.size() + " urls are empty or unconfigured");
		log("The empty urls or unconfigured urls are listed below:");
		emptyLst.forEach(t -> log((String) t));

		log("*******************************************************");
		log("A total of " + brokenLst.size() + " links are broken");
		log("The broken links are listed below:");
		brokenLst.forEach(t -> log((String) t));

	}


	@AfterMethod(alwaysRun = true)
	public void tearUp() {
		driver.close();
	}
}
