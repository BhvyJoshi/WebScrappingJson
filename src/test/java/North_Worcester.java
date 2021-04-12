import com.ASC.Common.BarnstableHelperClass;
import com.ASC.Common.InitializerClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;

import java.util.concurrent.TimeUnit;

public class North_Worcester extends BarnstableHelperClass { //Done need to change date format

    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "North Worcester";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;

    @Test
    public void test(){
        driver= InitializerClass.initialize(url,value);
        firstPage(driver,keyWord);
        tableData(driver,fileName);
    }

    public void firstPage(WebDriver driver,String keyWord) //it will give all towns data, if code will be uncommented then we can select the town.
    {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
       /* driver.get("https://www.barnstabledeeds.org/free-public-access/");
        driver.findElement(By.xpath("//*[@id=\"text-6\"]/div/a/img")).click();*/
       // driver.findElement(By.xpath("/html/body/div/div[2]/div/div[4]/div/div[3]/div[2]/div[2]/div/a")).click();
        //

        driver.findElement(By.xpath("/html/body/div/div[2]/div[1]/div[4]/div/div/div[4]/div[2]/div[2]/div/a")).click();
        driver.findElement(By.xpath("//*[@id=\"W9SNM\"]")).sendKeys(keyWord);
       /* Select drop = new Select(driver.findElement(By.xpath("//*[@id=\"W9TOWN\"]")));
        drop.selectByVisibleText("All towns");*/
        driver.findElement(By.xpath("//*[@id=\"search\"]/div/input")).click();
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }

}
