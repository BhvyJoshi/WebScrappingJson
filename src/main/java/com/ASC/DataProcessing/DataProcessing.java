package com.ASC.DataProcessing;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DataProcessing extends GrantorData{
    public static final String headerTagPath ="//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    public static final String mainTablePath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table";
    public static final String nextButtonPath = "//*[@id=\"DocList1_LinkButtonNext\"]";

    public String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath(headerTagPath));
        List<WebElement> headers = headerTag.findElements(By.tagName("th"));
        String[] header = new String[headers.size()];
        for (int i =0;i<headers.size();i++)
        {
            header[i]=headers.get(i).getText();
        }
        header = ArrayUtils.remove(header,0);
        header = modifyHeader(header);
        return header;
    }

    private String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Type Desc.","Type_Desc");
        header = header.replace("Type","Type__c").replace("Name/ Corporation","Name_Corporation__c");
        header = header.replace("Book","Book__c").replace("Page","Page__c").replace("Type__c_Desc","Type_Desc__c");
        header = header.replace("Rec. Date","Rec_Date__c").replace("Street #","Street__c");
        header = header.replace("Property Descr","Property_Descr__c").replace("Town","Town__c");
        header = header.replace("[","").replace("]","");
        hdr = header.split(",");
        return hdr;
    }

    public void tableData(WebDriver driver,String fileName)
    {
        String[] headers = grabHeader(driver);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers);

        boolean checkNext = true;

        while(checkNext){
            try{
                Thread.sleep(1000);
                WebElement nextBtn = driver.findElement(By.xpath(nextButtonPath));
                nextBtn.click();
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers));
            }
            catch (Exception e1){
                checkNext = false;
            }
        }

        JSONObject jsonObj = generateJson(tableDataContent);

        try {
            File myObj = new File("C:\\JsonResponse\\"+fileName+".txt");
            if(myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter("C:\\JsonResponse\\"+fileName+".txt");
                myWriter.write(jsonObj.toString());
                myWriter.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public JSONArray grabData(WebDriver driver,String[] header)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        JSONObject staticData = new JSONObject();
        staticData.put("type", "Lead_Search__c");
        staticData.put("referenceId","ref");

        WebElement table =  driver.findElement(By.xpath(mainTablePath));
        int rowSize = table.findElements(By.tagName("tr")).size();

        for (int rowCount=1;rowCount<=rowSize;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));

            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int column = 0, hdr = 0; (column < cols.size()); column++, hdr++) {
                if (column != 0) {
                    objForRow.put(header[hdr - 1], cols.get(column).getText());
                }
            }
            objForRow.put("attributes",staticData);
            objForRow.put("Grantors__r",getGrantorData(driver,rowCount));
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
    }

    public JSONObject generateJson(JSONArray jsonArray)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("records", jsonArray);
        return jsonObject;
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }

}