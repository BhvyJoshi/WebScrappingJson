
import com.ASC.Common.InitializerClass;
import com.ASC.Common.SouthEssexHelperClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class South_Essex extends SouthEssexHelperClass {
    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "South Essex";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    //public static String firstName = "abc";


    @Test
    public void test(){
        driver = InitializerClass.initialize(url,value);
        firstPage(driver,keyWord);
        grabHeader(driver);
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }

}
