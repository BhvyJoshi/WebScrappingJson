package com.ASC.HeaderProcessing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Arrays;

public class Group1 {

    public static final String headerTagPath ="//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";

    public String[] grabHeader(WebDriver driver)
    {
        String[] header = new String[9];
        new WebDriverWait(driver,20).until(ExpectedConditions.presenceOfElementLocated(By.xpath(headerTagPath+"/th")));
        for (int i =0;i<9;i++)
        {
            header[i]=driver.findElement(By.xpath(headerTagPath+"/th["+(i+2)+"]")).getText();
        }
        return modifyHeader(header);
    }

    public String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Type Desc.","Type_Desc");
        header = header.replace("Type","Type__c").replace("Name/ Corporation","Name__c");
        header = header.replace("Book","Book__c").replace("Page","Page__c").replace("Type__c_Desc","Type_Desc__c");
        header = header.replace("Rec. Date","Rec_Date__c").replace("Street #","Street__c");
        header = header.replace("Property Descr","Property_Descr__c").replace("Town","Town__c");
        header = header.replace("[","").replace("]","");
        return header.split(",");
    }
}
