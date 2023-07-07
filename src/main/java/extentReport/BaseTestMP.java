package extentReport;

import com.aventstack.extentreports.ExtentReports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.apache.commons.io.FileUtils;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BaseTestMP implements ITestListener {
	public static WebDriver driver;
	public static String screenshotsSubFolderName;
	public static ExtentReports extentReports;
	public static ExtentTest extentTest;
	String myurl = "";
	HttpURLConnection myhuc = null;
	int responseCode = 200;

	@BeforeTest
	public void setup(ITestContext context) {

		extentTest = extentReports.createTest(context.getName());
	
	}

	@AfterTest
	public void tearUp() {
		driver.quit();
	}

	@Parameters({ "browserName" })
	@BeforeSuite
	public void initialiseExtentReports() {
		ExtentSparkReporter sparkReporter_all = new ExtentSparkReporter("MemberPortalBrokenLinkAndImageTests.html");
		sparkReporter_all.config().setReportName("Member Portal Broken Link and Image Test");

		extentReports = new ExtentReports();
		extentReports.attachReporter(sparkReporter_all);

		extentReports.setSystemInfo("OS", System.getProperty("os.name"));
		extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));

	}

	@AfterSuite
	public void generateExtentReports() throws Exception {
		extentReports.flush();
	}

	@AfterMethod
	public void checkStatus(Method m, ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.fail(m.getName() + " has failed");
			extentTest.fail(result.getThrowable());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			extentTest.pass(m.getName() + " has passed");
		}

		extentTest.assignCategory(m.getAnnotation(Test.class).groups());
	}

	public void logCapture(String description, String responseBody) {
		extentReports.createTest(description).log(Status.PASS, responseBody);
		extentReports.flush();
	}

	public void log(String description) {
		// extentTest.log(Status.INFO,description);
		extentTest.log(Status.PASS, description);
	}

	@SuppressWarnings("unchecked")
	public void testBrokenLinks(List mylinks) throws InterruptedException {
		ArrayList allList = new ArrayList();
		ArrayList emptyLst = new ArrayList();
		ArrayList anotherDomainLst = new ArrayList();
		ArrayList brokenLst = new ArrayList();
		ArrayList myDomainLst = new ArrayList();
		WebDriver driver;
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

	public void testBrokenImages() {
		Integer iBrokenImageCount = 0;

		String status = "passed";
		try {
			iBrokenImageCount = 0;
			List<WebElement> image_list = driver.findElements(By.xpath("//img"));

			// System.out.println("The page under test has " + image_list.size() + "
			// image/s");
			log("The page under test has " + image_list.size() + " image/s");
			for (WebElement img : image_list) {
				if (img != null) {
					CloseableHttpClient client = HttpClientBuilder.create().build();
					HttpGet request = new HttpGet(img.getAttribute("src"));
					CloseableHttpResponse response = client.execute(request);

					if (response.getCode() != 200) {
						// System.out.println(img.getAttribute("outerHTML") + " is broken.");
						log("The broken image is :" + img.getAttribute("outerHTML"));
						iBrokenImageCount++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = "failed";
			// System.out.println(e.getMessage());
		}
		status = "passed";
		// System.out.println("The page " + "has " + iBrokenImageCount + " broken
		// image/s");
		log("The page " + "has " + iBrokenImageCount + " broken image/s");
	}
}
