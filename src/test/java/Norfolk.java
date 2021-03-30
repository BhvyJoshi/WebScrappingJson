import com.ASC.Common.InitializerClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;

public class Norfolk {

    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "Norfolk";
    public static String keyWord = "lender";
    public static String fileName = "demobarnstable";

    @Test
    public void test(){

    }
    @AfterTest
    public void cleanup(WebDriver driver)
    {
        InitializerClass.clenUp(driver);
    }

}
