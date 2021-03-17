import com.ASC.Common.BusinessAndFirstNameHelperClass;
import com.ASC.Common.InitilizerClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ClassWithFirstName extends BusinessAndFirstNameHelperClass {
    @Parameters({"url","value","keyWord","firstName","fileName"})
    @Test
    public void firstName(String url,String value,String keyWord,String firstName, String fileName)
    {
        driver = InitilizerClass.initialize(url,value);
        firstPage(driver,keyWord,firstName,fileName);
    }

    @AfterTest
    public void tearDown()
    {
        InitilizerClass.clenUp(driver);
    }
}
