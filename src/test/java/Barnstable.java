import com.ASC.Common.BarnstableHelperClass;
import com.ASC.Common.InitializerClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public class Barnstable extends BarnstableHelperClass{

    public WebDriver driver;

    /*public static String url = "https://www.masslandrecords.com";
    public static String value= "Barnstable";
    public static String keyWord = "lender";
    public static String fileName = "demobarnstable";*/



    @Test
    @Parameters({"url","value","keyWord","fileName","request"})
    public void test1() //Need to handle reqestID
    {
          String url = "https://www.masslandrecords.com";
          String value= "Barnstable";
          String keyWord = "lender";
          String fileName = "demo_"+value;
          String rrquestID ="123456";

        driver= InitializerClass.initialize(url,value);
        FirstPageForBarnstable(driver,keyWord);
        tableData(driver,fileName);
    }

    //9 headers 11 data columns and need 7 data

    @AfterTest
    public void tearDown()
    {
        driver.close();
        driver.quit();

        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println("\n JVM Name = " + jvmName);
        long pid = Long.valueOf(jvmName.split("@")[0]);
        System.out.println("JVM PID  = " + pid);
    }
}
