package com.ASC.ExecutableClasses;

import com.ASC.Common.BusinessAndFirstNameHelperClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ClassWithBusiness extends BusinessAndFirstNameHelperClass {

    @Parameters({"url","value","keyWord","fileName"})
    @Test
    public void withoutFirstName(String url,String value,String keyWord,String fileName)
    {
        initialize(url,value);
        firstPage(driver,keyWord,fileName);
    }

    @AfterTest
    public void cleanup()
    {
        driver.close();
        driver.quit();
    }
}
