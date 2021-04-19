import com.ASC.Common.BusinessAndFirstNameHelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ClassWithFirstName extends BusinessAndFirstNameHelperClass {
//No need of this class
    public WebDriver driver;

    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    @Test
    public void firstName(String url,String value,String keyWord,String firstName, String fileName,String request) {
        driver = InitializerClass.initialize(url,value);
        firstPage(driver,keyWord,firstName,fileName,request);
    }

    @AfterTest
    public void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
