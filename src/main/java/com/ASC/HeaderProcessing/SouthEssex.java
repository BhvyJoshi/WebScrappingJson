package com.ASC.HeaderProcessing;

import com.ASC.DataProcessing.CommonMethods;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class SouthEssex extends CommonMethods {

    public static final String headerXpath = "//*[@id=\"ASPxGridView1_DXHeadersRow0\"]";

    public String[] grabHeader(WebDriver driver){
        List<WebElement> headers = driver.findElement(By.xpath(headerXpath)).findElements(By.tagName("td"));
        String[] header = new String[headers.size()];
        for (int i =0;i<headers.size();i++)
        {
            header[i]=headers.get(i).getText();
        }
        header = Arrays.toString(header).replace("\n","").replace("[","").replace("]","").split(",");
        for (int i = 0; i < header.length; i++){
            if(!header[i].trim().equals("") || header[i]!=null)
                header[i] = header[i].trim();
        }
        header = Arrays.stream(header).distinct().toArray(String[]::new);
        header = ArrayUtils.removeAll(header,0,1,2);
        return modifyHeader(header);
    }

    private String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("DATE","DATE__c").replace("ROLE","ROLE__c");
        header = header.replace("Type","Type__c").replace("FIRST PARTY NAME","FIRST_PARTY_NAME__c");
        header = header.replace("Book","Book__c").replace("Page","Page__c");
        header = header.replace("DESC","DESC__c").replace("Street","Street__c");
        header = header.replace("Locus","Locus__c").replace("PBK","PBK__c");
        header = header.replace("PPG","PPG__c").replace("Consideration","Consideration__c");
        header = header.replace("SECOND PARTY NAME","SECOND_PARTY_NAME__c").replace("Town","Town__c");
        header = header.replace("[","").replace("]","");
        return header.split(",");
    }
}
