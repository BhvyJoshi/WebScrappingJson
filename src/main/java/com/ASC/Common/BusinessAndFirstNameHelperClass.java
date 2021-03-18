package com.ASC.Common;

import com.ASC.DataProcessing.dataprocessing_1;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class BusinessAndFirstNameHelperClass extends dataprocessing_1 {
    public WebDriver driver;


    public void firstPage(WebDriver driver,String keyWord,String fileName)
    {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_ACSTextBox_LastName1\"]")).sendKeys(keyWord);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_btnSearch\"]")).click();
        tableData(driver,fileName);
    }

    public void firstPage(WebDriver driver,String keyWord,String firstName,String fileName)
    {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_ACSTextBox_LastName1\"]")).sendKeys(keyWord);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_ACSTextBox_FirstName1\"]")).sendKeys(firstName);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_btnSearch\"]")).click();
        tableData(driver,fileName);
    }

}
