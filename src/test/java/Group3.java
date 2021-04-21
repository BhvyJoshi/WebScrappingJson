import com.ASC.Common.Group3HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class Group3 extends Group3HelperClass {

    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "South Bristol"/*"Fall River Bristol"*/;
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    private static String requestId = "1234";



    @Test
    public void test(){
        driver = InitializerClass.initialize(url,value);
        if(value == "South Bristol"){
            driver.get("https://i2e.uslandrecords.com/MA/BristolSouth/D/Default.aspx");
        }
        firstPage(driver, keyWord, fileName, requestId);
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}
