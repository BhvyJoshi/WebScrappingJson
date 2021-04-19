import org.openqa.selenium.WebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class FallRiverbristol {

    public WebDriver driver;
    @Parameters({"url","value","keyWord","firstName","fileName","request"})
    @Test
    public void test(String url,String value,String keyWord,String firstName, String fileName,String request){
        if(firstName == null){
            withoutFirstName(url, value, keyWord, fileName, request);
        }else{
            firstName(url, value, keyWord, firstName,  fileName, request);
        }
    }

    private void firstName(String url, String value, String keyWord, String firstName, String fileName, String request) {
    }

    private void withoutFirstName(String url, String value, String keyWord, String fileName, String request) {
    }
}
