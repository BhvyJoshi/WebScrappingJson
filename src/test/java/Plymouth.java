import com.ASC.Common.InitializerClass;
import com.ASC.Common.PlymouthHelperClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class Plymouth extends PlymouthHelperClass {
/*
    public static String url = "https://www.masslandrecords.com";
    public static String value= "Plymouth";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    public static String request = "12345";*/

    public static final String searchButtonClick = "//*[@id=\"SearchFormEx1_btnSearch\"]";
    public static final String lastNameTextBox = "//*[@id=\"SearchFormEx1_ACSTextBox_LastName1\"]";

    public WebDriver driver;
    @Test
    @Parameters({"url","value","keyWord","fileName","request"})
    public void PlymouthCounty(String url,String value,String keyWord,String fileName,String request){
   /* public void test(){*/
        String logFileName = value+"_"+fileName+"_"+request;
        try {
            driver = InitializerClass.initialize(url, value);
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            driver.findElement(By.xpath(lastNameTextBox)).sendKeys(keyWord);
            driver.findElement(By.xpath(searchButtonClick)).click();
            writeLog("---------------- Search is clicked--------",logFileName);
            tableData(driver, fileName, request,logFileName);
        }catch (Exception e){
            writeLog(e.toString(),logFileName);
        }
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}
