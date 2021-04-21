package com.ASC.HeaderProcessing;

import com.ASC.DataProcessing.CommonMethods;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class Cook extends CommonMethods {

    public String[] getHeader(WebDriver driver, String xPath) {
        WebElement headerLine = driver.findElement(By.xpath(xPath));
        List<WebElement> headers = headerLine.findElements(By.tagName("th"));
        String[] header = new String[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            header[i] = headers.get(i).getText();
        }
        return header;
    }

    public String[] grabHeader(WebDriver driver, String XPath){ //can be used for first page's data
        String dummyHeader = Arrays.toString(getHeader(driver,XPath));
        dummyHeader = dummyHeader.replace(", ",",");
        dummyHeader = dummyHeader.replace("[","").replace("]","");
        dummyHeader = dummyHeader.replace("Name","Name__c").replace("Trust#","Trust_No__c");
        String[] result = dummyHeader.split(",");
        //removing last 2 headers as not required.
        return ArrayUtils.removeAll(result,2,3);
    }

    public String[] grabHeader(WebDriver driver, String XPath,int x){ // can be used for 2nd page's data
        String[] hdr = getHeader(driver,XPath);
        hdr = ArrayUtils.remove(hdr,x);
        String dummyHeader = Arrays.toString(hdr);
        dummyHeader = dummyHeader.replace(", ",",");
        dummyHeader = dummyHeader.replace("[","").replace("]","");
        dummyHeader = dummyHeader.replace("Grantor","Grantor__c").replace("Grantee","Grantee__c");
        dummyHeader = dummyHeader.replace("Doc. #","Doc_No__c").replace("Type Desc.","Type_Desc__c");
        dummyHeader = dummyHeader.replace("Recorded Date","Recorded_Date__c").replace("PIN","PIN__c");
        return dummyHeader.split(",");
    }
}
