import com.ASC.Common.InitializerClass;
import com.ASC.Common.NorthBristolhelperClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

//completely done --. need to ask about firstName
public class North_Bristol extends NorthBristolhelperClass
{
    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "North Bristol";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    public static String requestID = "123456";
    public static String firstName = "";

    @Test
    public void test(){
        driver = InitializerClass.initialize(url,value);
        if(firstName == null){
            firstPage(driver,keyWord);
        }else{
            firstPage(driver,keyWord,firstName);
        }
        tableData(driver,fileName,requestID);
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }

}
