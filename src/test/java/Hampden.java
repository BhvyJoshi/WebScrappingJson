import com.ASC.Common.HampdenHelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//completely Done. --> ask about first Name is not working as expected on webSite
public class Hampden extends HampdenHelperClass {

    public WebDriver driver;
   /* public static String url = "https://www.masslandrecords.com";
    public static String value= "Hampden";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    public static String firstName = "";
    public static String request = "12345";*/

    @Test
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    public void test(String url,String value,String keyWord, String firstName,String fileName,String request){
    /*public void test(){*/
        String logFileName = value+"_"+fileName+"_"+request;
        try{
        driver = InitializerClass.initialize(url, value);
        firstPageForHampden(driver, keyWord, firstName);
        tableData(driver, fileName,request,logFileName);
        }catch (Exception e){
            writeLog(e.toString(),logFileName);
        }
    }

    @AfterTest
    public void tearDown()
    {
        driver.close();
        driver.quit();
    }
}
