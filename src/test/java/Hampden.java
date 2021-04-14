import com.ASC.Common.BarnstableHelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;



//Not able to open the site. saying proxy timeout.
public class Hampden extends BarnstableHelperClass {

    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "Hampden";
    public static String keyWord = "lender";
    public static String fileName = "demo";

    @Test
    public void test(){
        driver = InitializerClass.initialize(url,value);
        firstPageForHampden(driver,keyWord);
        //fullTableData(driver,fileName);
    }



    @AfterTest
    public void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
