
import com.ASC.Common.InitializerClass;
import com.ASC.Common.SouthEssexHelperClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//completed.

public class South_Essex extends SouthEssexHelperClass {
    public WebDriver driver;
   /* public static String url = "https://www.masslandrecords.com";
    public static String value= "South Essex";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    public static String firstName = "abc";*/
    @Test
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    public void test(String url,String value,String keyWord, String firstName,String fileName,String request){
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
