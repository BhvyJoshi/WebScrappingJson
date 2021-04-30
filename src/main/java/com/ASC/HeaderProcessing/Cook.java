package com.ASC.HeaderProcessing;

import com.ASC.DataProcessing.CommonMethods;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

public class Cook extends CommonMethods {

    public String[] getHeader(WebDriver driver, String xPath) {
        new WebDriverWait(driver,30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xPath+"/th")));
        int headerSize = driver.findElement(By.xpath(xPath)).findElements(By.tagName("th")).size();
        String[] header = new String[headerSize];
        for (int i = 0; i <headerSize; i++) {
            header[i] = driver.findElement(By.xpath(xPath+"/th["+(i+1)+"]")).getText();
        }
        return header;
    }

    public String[] grabHeader(WebDriver driver, String XPath){ //can be used for first page's data
        String dummyHeader = Arrays.toString(getHeader(driver,XPath));
        dummyHeader = dummyHeader.replace(", ",",");
        dummyHeader = dummyHeader.replace("[","").replace("]","");
        dummyHeader = dummyHeader.replace("Name","Name__c").replace("Trust #","Trust__c");
        String[] result = dummyHeader.split(",");
        return ArrayUtils.removeAll(result,2,3);
    }

    public String[] grabHeader(WebDriver driver, String XPath,int x){ // can be used for 2nd page's data
        String[] hdr = getHeader(driver,XPath);
        hdr = ArrayUtils.remove(hdr,x);
        String dummyHeader = Arrays.toString(hdr);
        dummyHeader = dummyHeader.replace(", ",",");
        dummyHeader = dummyHeader.replace("[","").replace("]","");
        //dummyHeader = dummyHeader.replace("Grantor","Grantor__c").replace("Grantee","Grantee__c");
        dummyHeader = dummyHeader.replace("Doc. #","Book__c").replace("Type Desc.","Type_Desc__c");
        dummyHeader = dummyHeader.replace("Recorded Date","Rec_Date__c").replace("PIN","PIN__c");
        return dummyHeader.split(",");
    }
}
