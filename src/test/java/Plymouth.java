import com.ASC.Common.InitializerClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;

public class Plymouth {

    public static String url = "https://www.masslandrecords.com";
    public static String value= "Plymouth";
    public static String keyWord = "lender";
    public static String fileName = "demobarnstable";

    public WebDriver driver;
    @Test
    public void PlymouthCounty(String url,String value,String keyWord,String fileName)
    {
      driver = InitializerClass.initialize(url,value);
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}
