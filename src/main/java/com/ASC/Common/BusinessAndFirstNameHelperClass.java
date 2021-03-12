package com.ASC.Common;

import com.ASC.DataProcessing.dataProcessing;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

public class BusinessAndFirstNameHelperClass{
    public WebDriver driver;

  /*  public void initialize(String url,String value) {
        System.setProperty("webdriver.chrome.driver","C:\\Program Files\\Selenium\\Drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
        driver.manage().deleteAllCookies();
        Select drop = new Select(driver.findElement(By.xpath("//*[@id=\"RegistryMap1_RegistryListBox\"]")));
        drop.selectByVisibleText(value);
        driver.findElement(By.xpath("//*[@id=\"GoButton\"]")).click();

    }*/

    public void firstPage(WebDriver driver,String keyWord,String fileName)
    {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_ACSTextBox_LastName1\"]")).sendKeys(keyWord);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_btnSearch\"]")).click();
        dataProcessing.tableData(driver,fileName);
    }

    public void firstPage(WebDriver driver,String keyWord,String firstName,String fileName)
    {
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_ACSTextBox_LastName1\"]")).sendKeys(keyWord);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_ACSTextBox_FirstName1\"]")).sendKeys(firstName);
        driver.findElement(By.xpath("//*[@id=\"SearchFormEx1_btnSearch\"]")).click();
        dataProcessing.tableData(driver,fileName);
    }

}
