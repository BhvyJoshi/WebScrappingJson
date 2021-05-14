import com.ASC.Common.Group1HelperClass;
import com.ASC.Common.Group3HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//completely done.

public class Group3 extends Group3HelperClass {

    /*public static String url = "https://www.masslandrecords.com";
    public static String value= *//*"South Bristol"*//*"Fall River Bristol";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    private static final String request = "1234";
    private static final String firstName = "";*/

    public WebDriver driver;

    @Test
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    public void test(String url, String value, String keyWord, String firstName, String fileName, String request){
    //public void test(){
        String logFileName = value+"_"+fileName+"_"+request;
        try {
            driver = InitializerClass.initialize(url, value);
            if (value.contains("South Bristol")) {
                driver.get("https://i2e.uslandrecords.com/MA/BristolSouth/D/Default.aspx");
            }
            new Group1HelperClass().firstPage(driver, keyWord, firstName,logFileName);
            writeLog("---------------- Search is clicked--------",logFileName);
            tableData(driver, fileName, request, logFileName);
        }catch (Exception e){
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
