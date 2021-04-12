import com.ASC.Common.BarnstableHelperClass;
import com.ASC.Common.InitializerClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;

public class North_Essex extends BarnstableHelperClass { //handle with reqID and header and data

    public WebDriver driver;
    public static String url = "https://www.masslandrecords.com";
    public static String value= "North Essex";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;

    @Test
    @Parameters({"url","value","keyWord","fileName","request"})
    public void test(){
        driver= InitializerClass.initialize(url,value);
        firstPageSuit2(driver,keyWord);
        tableData(driver,fileName);
    }

    // 9 headers, 9 data columns and need is 7
    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }

}
