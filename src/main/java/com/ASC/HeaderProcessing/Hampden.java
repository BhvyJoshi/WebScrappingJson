package com.ASC.HeaderProcessing;

import com.ASC.DataProcessing.CommonMethods;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.Arrays;
import java.util.List;

public class Hampden extends CommonMethods {

    public final static String headerTagPath = "//*[@id=\"search\"]/div/div[5]/table/tbody/tr[1]/th[";

    public String[] grabHeader(WebDriver driver)
    {
        String[] header = new String[7];
        for (int i =0;i<7;i++)
        {
            header[i]=driver.findElement(By.xpath(headerTagPath+(i+1)+"]")).getText();
        }
        header = modifyHeader(header);
        return ArrayUtils.addAll(header,"Page__c","Type__c");
    }

    public String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Reverse Party","Reverse_Party__c");//.replace("Name","Name__c");
        header = header.replace("Town","Town__c").replace("Date Received","Rec_Date__c");
        header = header.replace("Book (page)","Book__c").replace("Document Type","Type_Desc__c");
        header = header.replace("Document Desc","Property_Descr__c");
        header = header.replace("[","").replace("]","");
        return  header.split(",");
    }
}
