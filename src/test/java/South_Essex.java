import com.ASC.Common.InitializerClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;

public class South_Essex {
    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "South Essex";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;

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
