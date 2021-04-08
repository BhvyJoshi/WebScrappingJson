import com.ASC.Common.CookCountyHelper;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;

import java.util.concurrent.TimeUnit;

public class CookCounty extends CookCountyHelper {

    WebDriver driver;
    @Test
    public void test_CookCounty()
    {
        String url = "http://162.217.184.82/i2/default.aspx?AspxAutoDetectCookieSupport=1";
        String value = "lender";
        String fileName = "cookCounty_demo";
        String reqID = "1234";
        driver = initializeMainPage(url);
        firstPage(driver,value);
        tableData(driver,fileName,reqID);
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
    public void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
