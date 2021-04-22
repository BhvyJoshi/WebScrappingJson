import com.ASC.Common.CookCountyHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class CookCounty extends CookCountyHelper {

    public WebDriver driver;
    public static final String url = "http://162.217.184.82/i2/";
    @Test
    //@Parameters({"keyWord","fileName","request"})
    //public void CookCountyMethod(String keyWord, String fileName, String request)

    // need to check
    public void test_cookCounty()
    {

        String keyWord = "lender";
        String fileName = "cookCounty_demo";
        String request = "1234";

        driver = initializeMainPage(url);
        firstPage(driver,keyWord);
        tableData(driver,fileName,request);
    }

    public WebDriver initializeMainPage(String url) {
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver","C:\\Selenium\\Drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        driver.findElement(By.xpath("//*[@id=\"Navigator1_SearchCriteria1_menuButton\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"Navigator1_SearchCriteria1_LinkButton01\"]")).click();
        return driver;
    }

    @AfterTest
    public void cleanUp()
    {
        driver.close();
        driver.quit();
    }
}
