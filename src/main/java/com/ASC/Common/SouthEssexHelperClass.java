package com.ASC.Common;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SouthEssexHelperClass {
    public static final String searchRecordsBtn= "//*[@id=\"ctl00_SampleContent_ImageButton1\"]";
    public static final String startDateText= "//*[@id=\"ASPxPageControl1_RecordedTab_NameSearchStart\"]";
    public static final String startDate = "1/2/1951";
    public static final String lastNameText = "//*[@id=\"ASPxPageControl1_RecordedTab_txtNSLastName\"]";
    public static final String firstNameText = "//*[@id=\"ASPxPageControl1_RecordedTab_txtNSFirstName\"]";
    public static final String searchBtn = "//*[@id=\"ASPxPageControl1_RecordedTab_cmdNameSearch\"]";

    public static final String nextBtnPanel = "//*[@id=\"ASPxGridView1\"]/tbody/tr/td/div[1]";
    public static final String nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[10]";
    public static final String headerXpath = "//*[@id=\"ASPxGridView1_DXHeadersRow0\"]";
    public static final String dataRow = "//*[@id=\"ASPxGridView1_DXDataRow15\"]";
    //first page data row 0 to 14,2nd page 15 to 31 and so on.....


    public void firstPage(WebDriver driver,String keyWord){
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.xpath(searchRecordsBtn)).click();
        driver.findElement(By.xpath(startDateText)).sendKeys(startDate);
        driver.findElement(By.xpath(lastNameText)).sendKeys(keyWord);
       // driver.findElement(By.xpath(firstNameText)).sendKeys("abc");
        driver.findElement(By.xpath(searchBtn)).click();
    }

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
