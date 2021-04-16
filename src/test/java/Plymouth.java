import com.ASC.Common.InitializerClass;
import com.ASC.Common.PlymouthHelperClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

//currently site is down.
public class Plymouth extends PlymouthHelperClass {

    public static String url = "https://www.masslandrecords.com";
    public static String value= "Plymouth";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    public static String requestID = "12345";


    public WebDriver driver;
    @Test
    public void PlymouthCounty()
    {
       String searchButtonClick = "//*[@id=\"SearchFormEx1_btnSearch\"]";
       String lastNameTextBox = "//*[@id=\"SearchFormEx1_ACSTextBox_LastName1\"]";

      driver = InitializerClass.initialize(url,value);
      driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
      driver.findElement(By.xpath(lastNameTextBox)).sendKeys(keyWord);
      driver.findElement(By.xpath(searchButtonClick)).click();

      tableData(driver,fileName,requestID);
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}
