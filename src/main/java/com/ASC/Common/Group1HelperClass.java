package com.ASC.Common;

import com.ASC.DataProcessing.DataProcessing;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class Group1HelperClass extends DataProcessing {
    public WebDriver driver;
    private static final String searchButtonClick = "//*[@id=\"SearchFormEx1_btnSearch\"]";
    private static final String lastNameTextBox = "//*[@id=\"SearchFormEx1_ACSTextBox_LastName1\"]";
    private static final String firstnameTextBox = "//*[@id=\"SearchFormEx1_ACSTextBox_FirstName1\"]";
    private static final String msgBox = "//*[@id=\"MessageBoxCtrl1_ContentContainer\"]";
    private static final String errMsg = "//*[@id=\"MessageBoxCtrl1_ErrorLabel1\"]";

    public void firstPage(WebDriver driver,String keyWord,String firstName,String logFileName){
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.findElement(By.xpath(lastNameTextBox)).sendKeys(keyWord);
        driver.findElement(By.xpath(firstnameTextBox)).sendKeys(firstName);
        driver.findElement(By.xpath(searchButtonClick)).click();
        writeLog("------------------------Got main page -------------------------",logFileName);

        //if(driver.findElement(By.xpath()))

    }

}
