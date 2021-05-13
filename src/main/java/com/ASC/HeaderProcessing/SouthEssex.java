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

public class SouthEssex extends CommonMethods {

    public String[] grabHeader(WebDriver driver){
        String[] header = new String[13];
        for (int i=0; i<=12; i++)
        {
            header[i]=driver.findElement(By.xpath("//*[@id=\"ASPxGridView1_col"+(i+1)+"\"]/table/tbody/tr/td[1]/table/tbody/tr/td")).getText();
        }
        return modifyHeader(header);
    }

    private String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("DATE","Rec_Date__c").replace("ROLE","type__c");
        header = header.replace("Type","Type_Desc__c").replace("FIRST PARTY NAME","Name");
        header = header.replace("Book","Book__c").replace("Page","Page__c");
        header = header.replace("DESC","Property_Descr__c").replace("Street","Street__c");
        header = header.replace("Locus","Locus__c").replace("PBK","PBK__c");
        header = header.replace("PPG","PPG__c").replace("Consideration","Consideration__c");
        header = header.replace("SECOND PARTY NAME","Reverse_Party__c").replace("Town","Town__c");
        header = header.replace("[","").replace("]","").replace("type__c","Type__c");
        return header.split(",");
    }
}
