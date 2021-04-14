import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class South_Bristol {
    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "South Bristol";
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
