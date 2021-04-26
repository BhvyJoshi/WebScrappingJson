import com.ASC.Common.Group1HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//contains support for :--> Dukes, Franklin, Middle Berkshire, Nantucket, Suffolk
// North Berkshire, North Middlesex, Hampshire, South Berkshire, South Middlesex, Worcester
//with and without firstName

// add support for correctness of data
public class Group1 extends Group1HelperClass {
    public WebDriver driver;
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    @Test
    public void test(String url, String value, String keyWord, String firstName, String fileName, String request){
        driver = InitializerClass.initialize(url,value);
        firstPage(driver,keyWord,firstName);
        tableData(driver,fileName,request);
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}
