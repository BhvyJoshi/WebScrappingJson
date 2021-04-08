import com.ASC.Common.BusinessAndFirstNameHelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class ClassWithBusiness extends BusinessAndFirstNameHelperClass {

    public WebDriver driver;
    @Parameters({"url","value","keyWord","fileName","request"})
    @Test
    public void withoutFirstName(String url,String value,String keyWord,String fileName,String request){
 /*  public void test(){

        String url ="https://www.masslandrecords.com";
        String keyWord = "lender";
        String fileName = "demo_";
        String value = "Nantucket";*/

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
