package com.ASC.HeaderProcessing;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.Arrays;
import java.util.List;

public class Plymouth {

    public static final String headerTagPath ="//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    public String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath(headerTagPath));
        List<WebElement> headers = headerTag.findElements(By.tagName("th"));
        String[] header = new String[headers.size()];
        for (int i =0;i<headers.size();i++)
        {
            header[i]=headers.get(i).getText();
        }
        return modifyHeader(ArrayUtils.remove(header,0));
    }

    private String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Party","Party__c").replace("Reverse Party__c","Reverse_Party__c");
        header = header.replace("Type","Type__c").replace("Name","Name__c");
        header = header.replace("Book","Book__c").replace("Page","Page__c");
        header = header.replace("Rec Date","Rec_Date__c").replace("Street","Street__c");
        header = header.replace("Doc. #","Doc__c").replace("Town","Town__c");
        header = header.replace("[","").replace("]","");
        return header.split(",");
    }
}
