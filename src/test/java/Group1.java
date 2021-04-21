import com.ASC.Common.Group1HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//contains support for :--> Dukes, Fall River Bristol, Franklin, Middle Berkshire, Nantucket, Suffolk
// North Berkshire, North Middlesex, Hampshire, South Berkshire, South Middlesex, Worcester
//with and without firstName

public class Group1 extends Group1HelperClass {
    public WebDriver driver;
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    @Test
    public void test(String url, String value, String keyWord, /*@Optional*/ String firstName, String fileName, String request){
        if(firstName == null){
            withoutFirstName(url, value, keyWord, fileName, request);
        }else{
            firstName(url, value, keyWord, firstName,  fileName, request);
        }
    }

    protected void firstName(String url,String value,String keyWord,String firstName, String fileName,String request) {
        driver = InitializerClass.initialize(url,value);
        firstPage(driver,keyWord,firstName,fileName,request);
    }
    protected void withoutFirstName(String url,String value,String keyWord,String fileName,String request){
        driver = InitializerClass.initialize(url,value);
        firstPage(driver,keyWord,fileName,request);
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}
