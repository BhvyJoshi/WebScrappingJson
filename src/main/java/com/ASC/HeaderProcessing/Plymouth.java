package com.ASC.HeaderProcessing;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

public class Plymouth {

    public static final String headerTagPath ="//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    public String[] grabHeader(WebDriver driver)
    {
        new WebDriverWait(driver,30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(headerTagPath+"/th")));

        String[] header = new String[10];
        for (int i =0;i<10;i++)
        {
            header[i]=driver.findElement(By.xpath(headerTagPath+"/th["+(i+2)+"]")).getText();
        }
        return modifyHeader(header);
    }

    private String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Party","ptype__c").replace("Reverse ptype__c","Reverse_Party__c");
        header = header.replace("Type","Type_Desc__c").replace("ptype__c","Type__c");
        header = header.replace("Book","Book__c").replace("Page","Page__c");
        header = header.replace("Rec Date","Rec_Date__c").replace("Street","Street__c");
        header = header.replace("Doc. #","Doc__c").replace("Town","Town__c");
        header = header.replace("[","").replace("]","");
        return header.split(",");
    }
}
