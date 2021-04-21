import com.ASC.Common.Group2HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

//contains Support for: -->  Branstable,Norfolk,North Essex
//with and without firstName
public class Group2 extends Group2HelperClass {

    public WebDriver driver;
    public  String searchRegistryRecordClick;

    @Test
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    public void test1()
    {
          String url = "https://www.masslandrecords.com";
          String value=/*"Norfolk"*//*"North Essex"*//*"Barnstable",*/"North Worcester"; //need to check with Norfolk,North Worcester
          String keyWord = "lender";
          String fileName = "demo_"+value;
          String request ="123456";
          String firstName = /*"barbara"*//*not working*//*"Nadine"*/"";

        driver= InitializerClass.initialize(url,value);
        switch (value) {
            case "Barnstable":
                searchRegistryRecordClick = "/html/body/div/div[2]/div/div[4]/div/div[3]/div[2]/div[2]/div/a";

                driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
                driver.get("https://www.barnstabledeeds.org/free-public-access/");
                driver.findElement(By.xpath("//*[@id=\"text-6\"]/div/a/img")).click();
                checkFirstName(driver,keyWord,searchRegistryRecordClick,firstName);
                break;
            case "Norfolk":

            case "North Essex":
                searchRegistryRecordClick = "/html/body/div/div[2]/div/div[4]/div/div[3]/div[2]/div[2]/div/a";

                checkFirstName(driver,keyWord,searchRegistryRecordClick,firstName);
                break;
                 case "North Worcester":

                searchRegistryRecordClick = "/html/body/div/div[2]/div[1]/div[4]/div/div/div[4]/div[2]/div[2]/div/a";
                firstPageSuit2(driver, keyWord, searchRegistryRecordClick);
                //have 9 headers and 9 dATA COLUMNS BUT WE NEED 7 EACH
                break;
        }
        tableData(driver,fileName,request);
    }

    @AfterTest
    public void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
