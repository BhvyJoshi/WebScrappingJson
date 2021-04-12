import com.ASC.Common.BarnstableHelperClass;
import com.ASC.Common.InitializerClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class Norfolk extends BarnstableHelperClass {

    public WebDriver driver;

    public static String url = "https://www.masslandrecords.com";
    public static String value= "Norfolk";
    public static String keyWord = "lender";
    public static String fileName = "demo_"+value;

    @Test //handle with request ID
    @Parameters({"url","value","keyWord","fileName","request"})
    public void test(){
        driver= InitializerClass.initialize(url,value);
        firstPageSuit2(driver,keyWord);
        tableData(driver,fileName);
    }

    //9 headers 11 data columns and need 7 data
    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();

        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println("JVM Name = " + jvmName);
        long pid = Long.valueOf(jvmName.split("@")[0]);
        System.out.println("JVM PID  = " + pid);
    }

}
