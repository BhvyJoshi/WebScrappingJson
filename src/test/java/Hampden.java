import com.ASC.Common.HampdenHelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;



//Not able to open the site. saying proxy timeout.
//completely Done. --> ask about first Name is not working as expected on webSite
public class Hampden extends HampdenHelperClass {

    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "Hampden";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    public static String firstName = "";

    @Test
    public void test(){
        driver = InitializerClass.initialize(url,value);
        if(firstName == null) {
            firstPageForHampden(driver, keyWord);
        }else{
            firstPageForHampden(driver, keyWord,firstName);
        }
        tableData(driver,fileName,"12345");
    }

    @AfterTest
    public void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
