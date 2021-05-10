package com.ASC.HeaderProcessing;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

public class Group2 {
    private static final String headerXpath = "//*[@id=\"search\"]/div/table/tbody/tr[1]";

    public String[] grabHeader(WebDriver driver)
    {
        new WebDriverWait(driver,30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(headerXpath+"/th")));
        String[] header = new String[7];
        for (int i =0;i<7;i++)
        {
            header[i]=driver.findElement(By.xpath(headerXpath+"/th["+(i+1)+"]")).getText();
        }
        header = new Hampden().modifyHeader(header);
        return ArrayUtils.addAll(header,"Page__c","Type__c");
    }

 /*   public String[] modifyHeader(String[] hdr) {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Name","Name__c");
        header = header.replace("Reverse Party","Reverse_Party__c").replace("Town","Town__c");
        header = header.replace("Date Received","Date_Received__C").replace("Document Type","Document_Type__c").replace("Document Desc","Document_Desc__c");
        header = header.replace("Book (page)","Book_(page)__c").replace("[","").replace("]","");
        return header.split(",");
    }*/
}
