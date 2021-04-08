import com.ASC.Common.BusinessAndFirstNameHelperClass;
import com.ASC.Common.InitializerClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ClassWithFirstName extends BusinessAndFirstNameHelperClass {
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    @Test
    public void firstName(String url,String value,String keyWord,String firstName, String fileName,String request) throws InterruptedException {
        driver = InitializerClass.initialize(url,value);
        firstPage(driver,keyWord,firstName,fileName,request);
    }

    @AfterTest
    public void tearDown()
    {
        InitializerClass.clenUp(driver);
    }
}
