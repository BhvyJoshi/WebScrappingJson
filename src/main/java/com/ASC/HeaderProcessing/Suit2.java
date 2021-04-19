package com.ASC.HeaderProcessing;

import com.ASC.DataProcessing.CommonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class Suit2 extends CommonMethods {

    private static final String headerXpath = "//*[@id=\"search\"]/div/table/tbody/tr[1]";

    public String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath(headerXpath));
        List<WebElement> headers = headerTag.findElements(By.tagName("th"));
        String[] header = new String[7];
        for (int i =0;i<7;i++)
        {
            header[i]=headers.get(i).getText();
        }
        return modifyHeader(header);
    }

    public String[] modifyHeader(String[] hdr) {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Name","Name__c");
        header = header.replace("Reverse Party","Reverse_Party__c").replace("Town","Town__c");
        header = header.replace("Date Received","Date_Received__C").replace("Document Type","Document_Type__c").replace("Document Desc","Document_Desc__c");
        header = header.replace("Book (page)","Book_(page)__c").replace("[","").replace("]","");
        hdr = header.split(",");
        return hdr;
    }
}
