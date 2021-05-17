import com.ASC.Common.Group1HelperClass;
import com.ASC.Common.Group2HelperClass;
import com.ASC.Common.Group3HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

/*@Ignore*/
public class demoTests extends Group1HelperClass {

    public WebDriver driver;
    public  String searchRegistryRecordClick;
    private static final String msgBox = "//*[@id=\"MessageBoxCtrl1_ContentContainer\"]";
    private static final String errMsg = "//*[@id=\"MessageBoxCtrl1_ErrorLabel1\"]";
    private static final String alertMessage = "Search criteria resulted in 0 hits. Please verify the search criteria and try again.";


    @Test(dataProvider ="Group1")
    public void testDemo(String value){

        String url ="https://www.masslandrecords.com";
        String keyWord = "aaaaaaa";
        String fileName = "demo_"+value;
        String request = "123456";
        String firstName = "";
        String logFileName = value+"_"+fileName+"_"+request;
        driver = InitializerClass.initialize(url,value);
        firstPage(driver,keyWord,firstName,logFileName); //group1
       /* if(value.contains("South Bristol")){
            driver.get("https://i2e.uslandrecords.com/MA/BristolSouth/D/Default.aspx");
        }
        new Group1HelperClass().firstPage(driver, keyWord, firstName); //group3*/

        /*switch (value) {
            case "Barnstable": //Done
                searchRegistryRecordClick = "$(\".homeButton a\")[0].click();";
                driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
                driver.get("https://www.barnstabledeeds.org/free-public-access/");
                driver.findElement(By.xpath("//*[@id=\"text-6\"]/div/a/img")).click();
                checkFirstName(driver,keyWord,searchRegistryRecordClick,firstName);
                break;
            case "Norfolk": //Done
                searchRegistryRecordClick = "$(\".homeButton a\")[0].click();";
                checkFirstName(driver,keyWord,searchRegistryRecordClick,firstName);
                break;
            case "North Essex": //Done

            case "North Worcester": //Done
                searchRegistryRecordClick = "$(\".homeButton a\")[1].click();";
                checkFirstName(driver,keyWord,searchRegistryRecordClick,firstName);
                break;
        }*/
        if (driver.findElement(By.xpath(msgBox)).isDisplayed()) {

            if(alertMessage.equals(driver.findElement(By.xpath(errMsg)).getText()))
            {
                generateEmptyFile(fileName);
            }
            //closeChromeInstance(driver);
        } else {
            tableData(driver, fileName, request, logFileName);
        }
    }

    @DataProvider(name = "Group1")
    public Object[][] dataProvFunc(){
        return new Object[][]{
                {"Hampshire"},{"Fall River Bristol"},{"Dukes"},{"Franklin"},{"Middle Berkshire"},{"Nantucket"},
                {"North Berkshire"},{"North Middlesex"},{"South Berkshire"},{"South Middlesex"},
                {"Worcester"},{"Suffolk"}
        };
    }

    @DataProvider(name = "Group2")
    public Object[][] dataProvFunc2(){
        return new Object[][]{
                {"Norfolk"},{"North Essex"},{"Barnstable"},{"North Worcester"}
        };
    }

    @DataProvider(name = "Group3")
    public Object[][] dataProvFunc3(){
        return new Object[][]{
                {"South Bristol"},{"Fall River Bristol"}
        };
    }

    @AfterMethod
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}


