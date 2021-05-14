import com.ASC.Common.CookCountyHelper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class CookCounty extends CookCountyHelper {

    public WebDriver driver;
    //public static final String url = "http://162.217.184.82/i2/";
    public static final String mainHeaderPath = "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    public static String[] header = new String[2];
    public static final String subHeaderXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    public static String[] subHeader = new String[6];
    private final static String groupListButton = "//*[@id=\"TabResultController1_tabItemGroupListtabitem\"]";

    @Test
    @Parameters({"url","keyWord","fileName","request"})
    public void CookCountyMethod(String url, String keyWord, String fileName, String request){
   /* public void test_cookCounty(){

        String keyWord = "lender";
        String fileName = "cookCounty_demo";
        String request = "1234";*/
        String logFileName = "CookCounty_"+fileName+"_"+request;
        try {
            driver = initializeMainPage(url);
            firstPage(driver, keyWord,logFileName);
            writeLog("------------------------Got first initial page -------------------------",logFileName);
            header = grabHeader(driver, mainHeaderPath);
            subHeader = getSubHeader(driver);
            writeLog("------------------------Got Headers and SubHeaders -------------------------",logFileName);
            driver.findElement(By.xpath(groupListButton)).click();
            driver.navigate().refresh();
            writeLog("------------------------Getting required data content -------------------------",logFileName);
            tableData(driver, fileName, request, header, subHeader, logFileName);
        }catch (Exception e){
            writeLog(e.toString(),logFileName);
        }
    }

    public WebDriver initializeMainPage(String url) {
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver","C:\\Selenium\\Drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        driver.findElement(By.xpath("//*[@id=\"Navigator1_SearchCriteria1_menuButton\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"Navigator1_SearchCriteria1_LinkButton01\"]")).click();
        return driver;
    }

    private String[] getSubHeader(WebDriver driver1){

        String grantor = driver1.findElement(By.xpath("//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr[1]/td[3]")).getText();

        if(Integer.parseInt(grantor)!=0)
        {
            driver1.findElement(By.xpath("//*[@id=\"NameList1_GridView_NameListGroup_ctl02_ctl02\"]")).click();
        }else{
            driver1.findElement(By.xpath("//*[@id=\"NameList1_GridView_NameListGroup_ctl02_ctl03\"]")).click();
        }
        return grabHeader(driver1,subHeaderXpath,0);
    }
    @AfterTest
    public void cleanUp()
    {
        driver.close();
        driver.quit();
    }
}
