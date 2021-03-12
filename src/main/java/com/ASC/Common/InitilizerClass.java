package com.ASC.Common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class InitilizerClass {

    public static void initialize(String url,String value) {
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver","C:\\Program Files\\Selenium\\Drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(url);
        driver.manage().deleteAllCookies();
        Select drop = new Select(driver.findElement(By.xpath("//*[@id=\"RegistryMap1_RegistryListBox\"]")));
        drop.selectByVisibleText(value);
        driver.findElement(By.xpath("//*[@id=\"GoButton\"]")).click();
    }
}
