package com.ASC.DataProcessing;

import com.ASC.HeaderProcessing.Group1;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataProcessing extends GrantorData{

    public static final String mainTablePath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table";
    public static final String nextButtonPath = "//*[@id=\"DocList1_LinkButtonNext\"]";

    public void tableData(WebDriver driver,String fileName,String request)
    {
        String[] headers = new Group1().grabHeader(driver);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,request);

        boolean checkNext = true;

        while(checkNext){
            try{
                Thread.sleep(1000);
                WebElement nextBtn = driver.findElement(By.xpath(nextButtonPath));
                nextBtn.click();
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,request));
            }
            catch (Exception e1){
                checkNext = false;
            }
        }
        generateFile(fileName,tableDataContent);

    }

    public JSONArray grabData(WebDriver driver,String[] header,String request)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();
        JSONObject attributes = new JSONObject();


        WebElement table =  driver.findElement(By.xpath(mainTablePath));
        int rowSize = table.findElements(By.tagName("tr")).size();

        for (int rowCount=1;rowCount<=rowSize;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));

            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int column = 0, hdr = 0; (column < cols.size()); column++, hdr++) {
                if (column != 0) {
                    objForRow.put(header[hdr - 1], cols.get(column).getText());
                    while(hdr-1 == 5){
                        Date dob;
                        try {
                            dob = new SimpleDateFormat("MM/dd/yyyy").parse(cols.get(column).getText());
                            String str = new SimpleDateFormat("yyyy-MM-dd").format(dob);
                            objForRow.put(header[hdr-1],str);
                            break;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            attributes.put("type", "Lead_Search_Result__c");
            attributes.put("referenceId","ref"+rowCount+"_"+new Random().nextInt(100000));
            objForRow.put("attributes",attributes);
            objForRow.put("Grantors__r",getGrantorData(driver,rowCount));
            objForRow.put("Lead_Search__c",request);
            objForPage.put(objForRow);
            objForRow = new JSONObject();
            attributes = new JSONObject();
        }
        return objForPage;
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }

}
