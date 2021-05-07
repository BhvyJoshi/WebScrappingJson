import com.ASC.Common.Group1HelperClass;
import com.ASC.Common.Group2HelperClass;
import com.ASC.Common.Group3HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

/*@Ignore*/
public class demoTests extends Group2HelperClass {

    public WebDriver driver;
    public  String searchRegistryRecordClick;
    @Test(dataProvider ="Group2")
    public void testDemo(String value){

        String url ="https://www.masslandrecords.com";
        String keyWord = "lender";
        String fileName = "demo_"+value;
        String request = "123456";
        String firstName = "";

        driver = InitializerClass.initialize(url,value);
        //firstPage(driver,keyWord,firstName); //group1
       /* if(value.contains("South Bristol")){
            driver.get("https://i2e.uslandrecords.com/MA/BristolSouth/D/Default.aspx");
        }
        new Group1HelperClass().firstPage(driver, keyWord, firstName); //group3*/

        switch (value) {
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
        }
        tableData(driver,fileName,request);
    }

    @DataProvider(name = "Group1")
    public Object[][] dataProvFunc(){
        return new Object[][]{
                /*{"Hampshire"},{"Fall River Bristol"},{"Dukes"},{"Franklin"},{"Middle Berkshire"},{"Nantucket"},
                {"North Berkshire"},{"North Middlesex"},{"South Berkshire"},{"South Middlesex"},*/
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


