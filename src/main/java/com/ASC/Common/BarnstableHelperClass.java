package com.ASC.Common;

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

public class BarnstableHelperClass {

    private static final String nextButtonPath =  "//*[@id=\"search\"]/div/div[3]/div[1]/div/a[3]";
    private final static String mainTablePath = "//*[@id=\"search\"]/div/table/tbody";
    private static final String headerXpath = "//*[@id=\"search\"]/div/table/tbody/tr[1]";
    public static final String checkNextData = "//*[@id=\"search\"]/div/table/tbody";

    public String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath(headerXpath));
        List<WebElement> headers = headerTag.findElements(By.tagName("th"));
        String[] header = new String[headers.size()-2];
        for (int i =0;i<headers.size()-2;i++)
        {
            header[i]=headers.get(i).getText();
        }
        header = modifyHeader1(header);
        return header;
    }

    public String[] modifyHeader1(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("Name","Name__c");
        header = header.replace("Reverse Party","Reverse_Party__c").replace("Town","Town__c");
        header = header.replace("Date Received","Date_Received__C").replace("Document Type","Document_Type__c").replace("Document Desc","Document_Desc__c");
        header = header.replace("Book (page)","Book_(page)__c").replace("[","").replace("]","");
        hdr = header.split(",");
        return hdr;
    }

    public void tableData(WebDriver driver,String fileName)
    {
        String[] headers = grabHeader(driver);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers);

        while(checkForData(driver)){
            try{
                Thread.sleep(1000);
                WebElement nextBtn = driver.findElement(By.xpath(nextButtonPath));
                nextBtn.click();
                System.out.print("\n------------next Button clicked----");
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers));
            }
            catch (Exception e1){
                e1.printStackTrace();
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

    private boolean checkForData(WebDriver driver){
        WebElement table = driver.findElement(By.xpath(checkNextData));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        WebElement column = rows.get(rows.size()-1).findElement(By.tagName("td"));
        String columnText = column.getText();
        if (columnText.contains("More names may be available"))
            return true;
        else
            return false;
    }


    public JSONArray grabData(WebDriver driver,String[] header)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        JSONObject attributes = new JSONObject();


        WebElement table =  driver.findElement(By.xpath(mainTablePath));
        int rowSize = table.findElements(By.tagName("tr")).size();

        for (int rowCount=2;rowCount<rowSize;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));

            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int column = 0, hdr = 0; (column < cols.size()-4); column++, hdr++) {
                if (column != 0) {
                    objForRow.put(header[hdr], cols.get(column).getText());

                    while(hdr == 3) {
                        Date dob;
                        try {
                            dob = new SimpleDateFormat("MM-dd-yyyy").parse(cols.get(column).getText());
                            String str = new SimpleDateFormat("yyyy-MM-dd").format(dob);
                            objForRow.put(header[hdr - 1], str);
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
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
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

    public String getMainTableRow(int count){
        return "//*[@id=\"search\"]/div/table/tbody/tr["+count+"]";
    }
}
