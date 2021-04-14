import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class North_Bristol
{
    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "Norfolk";
    public static String keyWord = "lender";
    public static String fileName = "demobarnstable";


    @Test
    public void test(){

    }
    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }

}
