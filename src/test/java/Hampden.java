import com.ASC.Common.HampdenHelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class Hampden extends HampdenHelperClass {

    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "Hampden";
    public static String keyWord = "lender";
    public static String fileName = "demo";

    @Test
    public void test(){
        driver = InitializerClass.initialize(url,value);
        firstPage(driver,keyWord,value);
        fullTableData(driver,fileName);
    }

    public void firstPage(WebDriver driver, String keyWord,String value)
    {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("https://search.hampdendeeds.com/html/Hampden/V3/search.html");
        driver.findElement(By.xpath("/html/body/section[1]/div/div[2]/div[1]/div/ul/li[2]/a")).click();
        driver.findElement(By.xpath("//*[@id=\"W9SNM\"]")).sendKeys(keyWord);
        Select drop = new Select(driver.findElement(By.xpath("//*[@id=\"W9TOWN\"]")));
        drop.selectByVisibleText(value);
        driver.findElement(By.xpath("//*[@id=\"search\"]/div[4]/div[2]/input[1]")).click();
    }

    @AfterTest
    public void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
