import com.ASC.Common.Group1HelperClass;
import com.ASC.Common.Group3HelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

//completely done.

public class Group3 extends Group3HelperClass {
    /*public static String url = "https://www.masslandrecords.com";
    public static String value= "South Bristol"*//*"Fall River Bristol"*//*;
    public static String keyWord = "aaaaaaaaaaaaaaa";
    public static String fileName = "demo_"+value;
    private static final String request = "1234";
    private static final String firstName = "";*/

    public WebDriver driver;
    private static final String msgBox = "//*[@id=\"MessageBoxCtrl1_ContentContainer\"]";
    private static final String errMsg = "//*[@id=\"MessageBoxCtrl1_ErrorLabel1\"]";
    private static final String alertMessage = "Search criteria resulted in 0 hits. Please verify the search criteria and try again.";

    @Test
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    public void test(String url, String value, String keyWord, String firstName, String fileName, String request){
    /*public void test(){*/
        String logFileName = value+"_"+fileName+"_"+request;
        try {
            driver = InitializerClass.initialize(url, value);
            if (value.equals("South Bristol")) {
                driver.get("https://i2e.uslandrecords.com/MA/BristolSouth/D/Default.aspx");
            }
            new Group1HelperClass().firstPage(driver, keyWord, firstName,logFileName);
            writeLog("---------------- Search is clicked--------",logFileName);
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
