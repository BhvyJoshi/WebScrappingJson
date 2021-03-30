package com.ASC.Common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InitializerClass {

    public static WebDriver initialize(String url,String value) {
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver","C:\\Selenium\\Drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
        driver.manage().deleteAllCookies();
        Select drop = new Select(driver.findElement(By.xpath("//*[@id=\"RegistryMap1_RegistryListBox\"]")));
        drop.selectByVisibleText(value);
        driver.findElement(By.xpath("//*[@id=\"GoButton\"]")).click();
        return driver;
    }

    public static void clenUp(WebDriver driver)
    {
        driver.close();
        driver.quit();
    }
}
