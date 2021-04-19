import com.ASC.Common.BusinessAndFirstNameHelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class ClassWithBusiness extends BusinessAndFirstNameHelperClass {

    public WebDriver driver;
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    @Test
    public void test(String url,String value,String keyWord,String firstName, String fileName,String request){
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
