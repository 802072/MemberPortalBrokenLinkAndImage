package extentReport;

import com.aventstack.extentreports.ExtentReports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
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
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BaseTest1 implements ITestListener {
	public static WebDriver driver;
	public static String screenshotsSubFolderName;
	public static ExtentReports extentReports;
	public static ExtentTest extentTest;

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
		// .log(Status.INFO, "<b><i>"+baseApi+"</i></b>")
		// .log(Status.INFO,"Request body is: "+requestBody)
		// .log(Status.INFO,"ResponseBody is: "+responseBody);
		// .log(Status.INFO, MarkupHelper.createCodeBlock(requestBody,
		// CodeLanguage.JSON))
		// .log(Status.INFO,MarkupHelper.createCodeBlock(responseBody,
		// CodeLanguage.JSON));

		extentReports.flush();
	}

	public void log(String description) {
		// extentTest.log(Status.INFO,description);
		extentTest.log(Status.PASS, description);
	}

//	 public String captureScreenshot(String fileName) {
//	 if(screenshotsSubFolderName == null) {
//	 LocalDateTime myDateObj = LocalDateTime.now();
//	 DateTimeFormatter myFormatObj =
//	 DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
//	 screenshotsSubFolderName = myDateObj.format(myFormatObj);
//	 }
//	
//	 TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
//	 File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
//	 File destFile = new File("./Screenshots/"+
//	 screenshotsSubFolderName+"/"+fileName);
//	 try {
//	 FileUtils.copyFile(sourceFile, destFile);
//	 } catch (IOException e) {
//	 e.printStackTrace();
//	 }
//	 System.out.println("Screenshot saved successfully");
//	 return destFile.getAbsolutePath();
//	 }
	// Creating a method getScreenshot and passing two parameters
	// driver and screenshotName

//	// screenshot
//	public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
//		// below line is just to append the date format with the screenshot name to
//		// avoid duplicate names
//		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
//		TakesScreenshot ts = (TakesScreenshot) driver;
//		File source = ts.getScreenshotAs(OutputType.FILE);
//		// after execution, you could see a folder "FailedTestsScreenshots" under src
//		// folder
//		String destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + screenshotName + dateName
//				+ ".png";
//		File finalDestination = new File(destination);
//		FileUtils.copyFile(source, finalDestination);
//		// Returns the captured file path
//		return destination;
//	}

}
