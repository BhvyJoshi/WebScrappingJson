import com.ASC.Common.InitializerClass;
import com.ASC.Common.NorthBristolhelperClass;
import com.ASC.DataProcessing.CommonMethods;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//completely done --. need to ask about firstName on site is not working
public class North_Bristol extends NorthBristolhelperClass {

  /*  public static String url = "https://www.masslandrecords.com";
    public static String value= "North Bristol";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    public static String request = "123456";
    public static String firstName = "";*/

    public WebDriver driver;

    @Test
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    public void test(String url,String value,String keyWord, String firstName,String fileName,String request){
    //public void test(){
        String logFileName = value+"_"+fileName+"_"+request;
        writeLog(" URL is ---->"+url+"-------------------------", logFileName);
        try{
        driver = InitializerClass.initialize(url,value);
        firstPage(driver,keyWord,firstName);
        writeLog("---------------- Search is clicked--------",logFileName);
        tableData(driver,fileName,request,logFileName);
        }
        catch (Exception e){
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
