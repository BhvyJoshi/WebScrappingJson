package com.ASC.Common;

import com.ASC.DataProcessing.GrantorData;
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

public class PlymouthHelperClass extends GrantorData {

    public static final String headerTagPath ="//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    public static final String mainTablePath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody";
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
        return modifyHeader(header);
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

    public void tableData(WebDriver driver,String fileName,String request)
    {
        String[] headers = grabHeader(driver);
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


        try {
            File myObj = new File("C:\\JsonResponse\\"+fileName+".txt");
            if(myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter("C:\\JsonResponse\\"+fileName+".txt");
                myWriter.write(tableDataContent.toString());
                myWriter.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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
                    while(hdr-1 == 7){
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
            //objForRow.put("Grantors__r",getGrantorData(driver,rowCount));
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

    public JSONArray appendToList(JSONArray original,JSONArray toBeAppend)
    {
        JSONArray sourceArray = new JSONArray(toBeAppend);
        JSONArray destinationArray = new JSONArray(original);

        for (int i = 0; i < sourceArray.length(); i++) {
            destinationArray.put(sourceArray.getJSONObject(i));
        }
        return destinationArray;
    }
}
