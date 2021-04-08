import com.ASC.Common.BarnstableHelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class Barnstable extends BarnstableHelperClass{
    public WebDriver driver;
    /*public static String url = "https://www.masslandrecords.com";
    public static String value= "Barnstable";
    public static String keyWord = "lender";
    public static String fileName = "demobarnstable";*/

    @Test
    public void test1()
    {
          String url = "https://www.masslandrecords.com";
          String value= "Barnstable";
          String keyWord = "lender";
          String fileName = "demobarnstable";

        driver= InitializerClass.initialize(url,value);
        firstPage(driver,keyWord,value);
        tableData(driver,fileName);
    }

    public void firstPage(WebDriver driver,String keyWord,String value)
    {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.get("https://www.barnstabledeeds.org/free-public-access/");
        driver.findElement(By.xpath("//*[@id=\"text-6\"]/div/a/img")).click();
        driver.findElement(By.xpath("/html/body/div/div[2]/div/div[4]/div/div[3]/div[2]/div[2]/div/a")).click();
        driver.findElement(By.xpath("//*[@id=\"W9SNM\"]")).sendKeys(keyWord);
        Select drop = new Select(driver.findElement(By.xpath("//*[@id=\"W9TOWN\"]")));
        drop.selectByVisibleText(value);
        driver.findElement(By.xpath("//*[@id=\"search\"]/div/input")).click();
    }

    @AfterTest
    public void tearDown()
    {
        InitializerClass.clenUp(driver);
    }
}
