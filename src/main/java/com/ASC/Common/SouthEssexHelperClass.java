package com.ASC.Common;

import com.ASC.HeaderProcessing.SouthEssex;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SouthEssexHelperClass extends SouthEssex {

    public static final String searchRecordsBtn= "//*[@id=\"ctl00_SampleContent_ImageButton1\"]";
    public static final String startDateText= "//*[@id=\"ASPxPageControl1_RecordedTab_NameSearchStart\"]";
    public static final String startDate = "1/2/1951";
    public static final String lastNameText = "//*[@id=\"ASPxPageControl1_RecordedTab_txtNSLastName\"]";
    public static final String firstNameText = "//*[@id=\"ASPxPageControl1_RecordedTab_txtNSFirstName\"]";
    public static final String searchBtn = "//*[@id=\"ASPxPageControl1_RecordedTab_cmdNameSearch\"]";
    public static final String searchResultCount = "//*[@id=\"dvSearchTerms\"]/table/tbody/tr[4]/td[1]";

    public void firstPage(WebDriver driver,String keyWord, String firstName){
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.xpath(searchRecordsBtn)).click();
        driver.findElement(By.xpath(startDateText)).sendKeys(startDate);
        driver.findElement(By.xpath(lastNameText)).sendKeys(keyWord);
        driver.findElement(By.xpath(firstNameText)).sendKeys(firstName);
        driver.findElement(By.xpath(searchBtn)).click();
    }

    private int getSearchResultCount(WebDriver driver){
        String result = driver.findElement(By.xpath(searchResultCount)).getText();
        result =  result.replace("Result:","").replace("Rows","").trim();
        return Integer.parseInt(result);
    }
    public void tableData(WebDriver driver,String fileName,String requestID)
    {
        String[] headers = grabHeader(driver);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,requestID,0,15);
        int searchResultCount = getSearchResultCount(driver);
        int noOfLoop = searchResultCount/15;
        int lastPageData = searchResultCount % 15;
        if(noOfLoop>10){
            int count =0;
            while(count<noOfLoop-1){
                try{
                    String nextBtnClick;
                    count ++;
                    Thread.sleep(1000);
                    if(count == 1) {
                        nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[10]";
                    }else {
                        nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[11]";
                    }
                    WebElement nextBtn = driver.findElement(By.xpath(nextBtnClick));
                    nextBtn.click();
                    Thread.sleep(2000);
                    tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID,(15*count),(15*count)+15));
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            driver.findElement(By.xpath("//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[11]")).click();
            tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID,(15*noOfLoop),(15*noOfLoop)+lastPageData));

        }else{
            int count =0;
            while(count<noOfLoop-1){
                try{
                    String nextBtnClick;
                    count ++;
                    Thread.sleep(1000);
                    if(count == 1) {
                        nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[2]";
                    }else {
                        nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a["+(count+1)+"]";
                    }
                    WebElement nextBtn = driver.findElement(By.xpath(nextBtnClick));
                    nextBtn.click();
                    Thread.sleep(2000);
                    tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID,(15*count),(15*count)+15));
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
            }
            driver.findElement(By.xpath("//*[@id=\"ASPxGridView1_DXPagerTop\"]/a["+(noOfLoop+1)+"]")).click();
            tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID,(15*noOfLoop),(15*noOfLoop)+lastPageData));

            //driver.findElement(By.xpath("//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[2]")).click();
        }


        generateFile(fileName,tableDataContent);
    }

    public JSONArray grabData(WebDriver driver,String[] header,String requestID,int lowerLimit, int upperLimit)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        JSONObject attributes = new JSONObject();
        for (int rowCount=lowerLimit;rowCount<upperLimit;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));
            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int hdr = 0; (hdr < header.length); hdr++) {
                //for (int column = 0, hdr = 0; (column < 7); column++, hdr++) {
                objForRow.put(header[hdr], cols.get(hdr+2).getText());
                while(hdr == 0) {
                    Date dob;
                    try {
                        dob = new SimpleDateFormat("MM/dd/yyyy").parse(cols.get(hdr+2).getText());
                        String str = new SimpleDateFormat("yyyy-MM-dd").format(dob);
                        objForRow.put(header[hdr], str);
                        break;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            attributes.put("type", "Lead_Search_Result__c");
            attributes.put("referenceId","ref"+rowCount+"_"+new Random().nextInt(100000));
            objForRow.put("attributes",attributes);
            objForRow.put("Lead_Search__c",requestID); //add request id
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
    }

     public String getMainTableRow(int count){
        return "//*[@id=\"ASPxGridView1_DXDataRow"+count+"\"]";
    }
}
