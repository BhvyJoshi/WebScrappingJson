import com.ASC.Common.InitializerClass;
import com.ASC.Common.Suit1HelperClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class South_Bristol extends Suit1HelperClass {
    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "South Bristol";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    private static String requestId = "1234";


    //need to remove last 2 columns from header ie. view img and add to basket.
    @Test
    public void test(){
        driver = InitializerClass.initialize(url,value);
        driver.get("https://i2e.uslandrecords.com/MA/BristolSouth/D/Default.aspx");
        firstPage(driver, keyWord, fileName, requestId);
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }

}
