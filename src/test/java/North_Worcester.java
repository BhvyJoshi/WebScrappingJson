import com.ASC.Common.BarnstableHelperClass;
import com.ASC.Common.InitializerClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;


public class North_Worcester extends BarnstableHelperClass { //Done need to change date format, handle header and column part

    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "North Worcester";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;
    public static final String xpath = "/html/body/div/div[2]/div[1]/div[4]/div/div/div[4]/div[2]/div[2]/div/a";

    @Test
    public void test(){
        driver= InitializerClass.initialize(url,value);
        firstPageSuit2(driver,keyWord,xpath);
        tableData(driver,fileName);
    }

    //9 headers, 9 data columns and need is 7

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }

}
