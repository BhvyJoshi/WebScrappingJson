import com.ASC.Common.Group1HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
/*
    public static String url = "https://www.masslandrecords.com";
    public static String value= *//*"Hampshire""Dukes""Worcester""Franklin"*//*"Nantucket"*//*"Suffolk""North Berkshire""North Middlesex""South Berkshire""South Middlesex"*//*;
    public static String keyWord = "aaaaaaa";
    public static String fileName = "demo_"+value;
    private static final String request = "1234";
    private static final String firstName = "";*/

    private static final String msgBox = "//*[@id=\"MessageBoxCtrl1_ContentContainer\"]";
    private static final String errMsg = "//*[@id=\"MessageBoxCtrl1_ErrorLabel1\"]";
    private static final String alertMessage = "Search criteria resulted in 0 hits. Please verify the search criteria and try again.";

    @Test
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    public void test(String url, String value, String keyWord, String firstName, String fileName, String request){
   /* public void test(){*/
        String logFileName = value+"_"+fileName+"_"+request;
        writeLog(" URL is ---->"+url+"-------------------------", logFileName);
        try {
            driver = InitializerClass.initialize(url, value);
            firstPage(driver, keyWord, firstName, logFileName);
            try {
                if(driver.findElement(By.xpath("//*[@id=\"DocList1_ToolBarContainer\"]")).isDisplayed());
                tableData(driver, fileName, request, logFileName);

            } catch (NoSuchElementException e) {
                if (driver.findElement(By.xpath(msgBox)).isEnabled()) {
                    if (alertMessage.equals(driver.findElement(By.xpath(errMsg)).getText())) {
                        generateEmptyFile(fileName);
                    } else {
                        generateTooManyResultsContent(fileName);
                    }
                }
                else {
                    //e.printStackTrace();
                    writeLog(e.toString(), logFileName);
                }
            }
        }catch(Exception e){
            writeLog(e.toString(), logFileName);
        }
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}
