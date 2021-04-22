import com.ASC.Common.Group1HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

/*@Ignore*/
public class demoTests extends Group1HelperClass {

    public WebDriver driver;
    @Test(dataProvider ="keyWords")
    public void testDemo(String value){

        String url ="https://www.masslandrecords.com";
        String keyWord = "lender";
        String fileName = "demo_"+value;
        String request = "123456";
        String firstName = "";

        driver = InitializerClass.initialize(url,value);
        firstPage(driver,keyWord,fileName,firstName,request);
    }

    @DataProvider(name = "keyWords")
    public Object[][] dataProvFunc(){
        return new Object[][]{
                {"Hampshire"},/*{"Fall River Bristol"},*/{"Dukes"},{"Franklin"},{"Middle Berkshire"},{"Nantucket"},
                {"North Berkshire"},{"North Middlesex"},{"South Berkshire"},/*{"South Middlesex"},
                {"Worcester"},{"Suffolk"}*/
        };
    }

    @AfterMethod
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}


