package com.ASC.HeaderProcessing;

import com.ASC.DataProcessing.CommonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class Hampden extends CommonMethods {

    public final static String headerTagPath = "//*[@id=\"search\"]/div/div[5]/table/tbody/tr[1]";

    public String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath(headerTagPath));
        List<WebElement> headers = headerTag.findElements(By.tagName("th"));
        String[] header = new String[headers.size()-2];
        for (int i =0;i<headers.size()-2;i++)
        {
            header[i]=headers.get(i).getText();
        }

        return modifyHeader(header);
    }

    private String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Reverse Party","Reverse_Party__c").replace("Name","Name__c");
        header = header.replace("Town","Town__c").replace("Date Received","Date_Received__c");
        header = header.replace("Book (page)","Book_(page)__c").replace("Document Type","Document_Type__c").replace("Document Desc","Document_Desc__c");
        header = header.replace("[","").replace("]","");
        System.out.println(header);
        hdr = header.split(",");
        return hdr;
    }
}
