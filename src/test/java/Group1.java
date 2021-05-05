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

   /* public static String url = "https://www.masslandrecords.com";
    public static String value=*//* "Hampshire"*//**//*"Dukes"*//**//*"Worcester"*//**//*"Franklin"*//*"Nantucket"*//*"Suffolk"*//**//*"North Berkshire"*//**//*"North Middlesex"*//**//*"South Berkshire"*//**//*"South Middlesex"*//*;
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    private static final String request = "1234";
    private static final String firstName = "";*/

    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    @Test
    public void test(String url, String value, String keyWord, String firstName, String fileName, String request){
   // public void test(){
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
