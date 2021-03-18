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
import java.util.stream.Stream;

public class dataprocessing_1 {

    public String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath("//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr"));
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
        header = header.replace("Type Desc.","Type_Desc");
        header = header.replace("Type","Type_c").replace("Name/ Corporation","Name_Corporation_c");
        header = header.replace("Book","Book_c").replace("Page","Page_c").replace("Type_c_Desc","Type_Desc_c");
        header = header.replace("Rec. Date","Rec_Date_c").replace("Street #","Street_c");
        header = header.replace("Property Descr","Property_Descr_c").replace("Town","Town_c");
        header = header.replace("[","").replace("]","");
        hdr = header.split(",");
        return hdr;
    }

    public void tableData(WebDriver driver,String fileName)
    {
        String[] headers = grabHeader(driver);
        JSONArray tableDataContaint;
        tableDataContaint = grabData(driver,headers);

        boolean checkNext = true;

        while(checkNext){
            try{
                WebElement nextBtn = driver.findElement(By.xpath("//*[@id=\"DocList1_LinkButtonNext\"]"));
                nextBtn.click();
                Thread.sleep(5000);
                tableDataContaint = appendToList(tableDataContaint,grabData(driver,headers));
            }
            catch (Exception e1){
                checkNext = false;
            }
        }

        JSONObject jsonObj = generateJson(tableDataContaint);

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

    public JSONArray appendToList(JSONArray original,JSONArray toBeAppend)
    {
        return original.put(toBeAppend);
    }

    public JSONArray grabData(WebDriver driver,String[] header)
    {

        JSONArray objForPage = new JSONArray();

        JSONObject staticData = new JSONObject();
        staticData.put("type", "Lead_Search_c");
        staticData.put("referenceId", "ref");

        JSONObject objForRow = new JSONObject();
        WebElement table =  driver.findElement(By.xpath("//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int column = 0, hdr = 0; (column < cols.size()); column++, hdr++) {
                if (column != 0) {
                    //call function of getting child data
                    //getGrantorData(driver);
                    objForRow.put(header[hdr - 1], cols.get(column).getText());
                }
            }
            objForRow.put("attributes",staticData);
            objForRow.put("Grentor_c",getGrantorData(driver,row,rows.indexOf(row)));
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
    }

    public JSONObject generateJson(JSONArray jsonArray) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("records", jsonArray);
        return jsonObject;
    }

    public JSONObject getGrantorData(WebDriver driver,WebElement row, int itr)
    {
         /* //*[@id="DocList1_GridView_Document_ctl03_ButtonRow_Name/ Corporation_1"]
        //*[@id="DocList1_GridView_Document_ctl04_ButtonRow_Name/ Corporation_2"]
        //*[@id="DocList1_GridView_Document_ctl21_ButtonRow_Name/ Corporation_19"]

        2nd page: //*[@id="DocList1_GridView_Document_ctl02_ButtonRow_Name/ Corporation_0"]

        main logic: no of rows in a page
        */

        String[] key = {"Name","Type_c"};
        JSONArray objForTable = new JSONArray();
        JSONObject objForRow = new JSONObject();

        int docIndex = 02+itr;
        row.findElement(By.xpath("//*[@id=\"DocList1_GridView_Document_ctl"+docIndex+"_ButtonRow_Name/ Corporation_"+itr+"\"]")).click();
        WebElement table = driver.findElement(By.xpath("//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        rows.remove(0);
        for (WebElement r:rows) {
            List<WebElement> cols = r.findElements(By.tagName("td"));
            for (int i = 0; i < cols.size(); i++) {
                objForRow.put(key[i],cols.get(i).getText());
            }
            objForTable.put(objForRow);
            objForRow = new JSONObject();
        }

        JSONObject mainObj = new JSONObject();
        mainObj.put("Grantor_c",objForTable);
        return mainObj;
    }
}
