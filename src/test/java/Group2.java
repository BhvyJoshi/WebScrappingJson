import com.ASC.Common.Group2HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

//contains Support for: -->  Branstable,Norfolk,North Essex,North Worcester
//Completed

public class Group2 extends Group2HelperClass {

    public WebDriver driver;
    public  String searchRegistryRecordClick;

    @Test
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    public void test1(String url,String value,String keyWord, String firstName,String fileName,String request){
    /*public void test()
    {
          String url = "https://www.masslandrecords.com";
          String value=*//*"Norfolk""North Essex""Barnstable"*//*"North Worcester"; //need to check with Norfolk,North Worcester
          String keyWord = "aaaaaaaaaaaaaaa";
          String fileName = "demo_"+value;
          String request ="123456";
          String firstName =""*//*"" "barbara"ot working"Nadine"*//*;*/
        String logFileName = value+"_"+fileName+"_"+request;
        try {

            driver = InitializerClass.initialize(url, value);
            switch (value) {
                case "Barnstable": //Done
                    searchRegistryRecordClick = "$(\".homeButton a\")[0].click();";
                    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
                    driver.get("https://www.barnstabledeeds.org/free-public-access/");
                    driver.findElement(By.xpath("//*[@id=\"text-6\"]/div/a/img")).click();
                    checkFirstName(driver, keyWord, searchRegistryRecordClick, firstName);
                    writeLog("----------------Registry Search btn and Search is clicked--------",logFileName);
                    break;
                case "Norfolk": //Done
                    searchRegistryRecordClick = "$(\".homeButton a\")[0].click();";
                    checkFirstName(driver, keyWord, searchRegistryRecordClick, firstName);
                    writeLog("----------------Registry Search btn and Search is clicked--------",logFileName);
                    break;
                case "North Essex": //Done

                case "North Worcester": //Done
                    searchRegistryRecordClick = "$(\".homeButton a\")[1].click();";
                    checkFirstName(driver, keyWord, searchRegistryRecordClick, firstName);
                    writeLog("----------------Registry Search btn and Search is clicked--------",logFileName);
                    break;
            }
            tableData(driver, fileName, request, logFileName);
        }catch(Exception e){
            writeLog(e.toString(),logFileName);
        }
    }

    @AfterTest
    public void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
