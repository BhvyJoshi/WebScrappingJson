import com.ASC.Common.InitializerClass;
import com.ASC.Common.SouthEssexHelperClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//Done
public class South_Essex extends SouthEssexHelperClass {
    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "South Essex";
    public static String keyWord = "aaaaaaaaaaaaaaaaaa";
    public static String fileName = "demo_"+value;
    public static String firstName = "julian";
    public static String request = "123";
    @Test
    /*@Parameters({"url","value","keyWord","firstName","fileName","request"})
    public void test(String url,String value,String keyWord, String firstName,String fileName,String request){*/
    public void test(){
        String logFileName = value+"_"+fileName+"_"+request;
        try {
            driver = InitializerClass.initialize(url, value);
            firstPage(driver, keyWord, firstName);
            writeLog("-------------------Search btn is chicked---------------",logFileName);
            tableData(driver, fileName, request,logFileName);
        }catch (Exception e)
        {
            writeLog(e.toString(),logFileName);
        }
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }

}
