import com.ASC.Common.BusinessAndFirstNameHelperClass;
import com.ASC.Common.InitilizerClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.annotation.Repeatable;

public class ClassWithBusiness extends BusinessAndFirstNameHelperClass {

    public WebDriver driver;
  // @Parameters({"url","value","keyWord","fileName"})
    @Test
   //public void withoutFirstName(String url,String value,String keyWord,String fileName){

    public void test() {
         String url = "https://www.masslandrecords.com";
         String value= "Hampshire";
         String keyWord = "lender";
         String fileName = "demo";

        driver = InitilizerClass.initialize(url,value);
        firstPage(driver,keyWord,fileName);
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}
