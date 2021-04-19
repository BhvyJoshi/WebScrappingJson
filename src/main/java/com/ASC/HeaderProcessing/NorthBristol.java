package com.ASC.HeaderProcessing;

import com.ASC.DataProcessing.CommonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class NorthBristol extends CommonMethods {

    public static final String headerTagPath = "//*[@id=\"ctl00_cphMainContent_gvSearchResults\"]/tbody/tr[2]";

    public String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath(headerTagPath));
        List<WebElement> headers = headerTag.findElements(By.tagName("th"));
        String[] header = new String[headers.size()-1];
        for (int i =0;i<headers.size()-1;i++)
        {
            header[i]=headers.get(i).getText();
        }
        return modifyHeader(header);
    }

    private String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("V","V__c").replace("Doc","Doc__c");
        header = header.replace("Type","Type__c").replace("Name","Name__c");
        header = header.replace("Book","Book__c").replace("Page","Page__c").replace("Type__c_Desc","Type_Desc__c");
        header = header.replace("Date","Date__c").replace("Name__cType__c","NameType__c");
        header = header.replace("Description","Description__c").replace("Town","Town__c");
        header = header.replace("[","").replace("]","");
        System.out.println(header);
        hdr = header.split(",");

        return hdr;
    }
}