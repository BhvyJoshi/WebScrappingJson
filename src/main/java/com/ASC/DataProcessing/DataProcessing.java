package com.ASC.DataProcessing;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.remote.internal.WebElementToJsonConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class DataProcessing {
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
        ArrayList<SortedMap<String,String>> tableDataContaint;
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

        JSONObject jsonObj = generateJson(tableDataContaint,headers);

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

    public ArrayList<SortedMap<String,String>> appendToList(ArrayList<SortedMap<String,String>> original,ArrayList<SortedMap<String,String>> toBeAppend)
    {
        ArrayList<SortedMap<String,String>> append = new ArrayList<>();
        Stream.of(original,toBeAppend).forEach(append::addAll);
        return append;
    }

    public ArrayList<SortedMap<String,String>> grabData(WebDriver driver,String[] header)
    {
        ArrayList<SortedMap<String,String>> list = new ArrayList<>();
        SortedMap<String,String> map= new TreeMap<>();
        WebElement table =  driver.findElement(By.xpath("//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int j = 0, itr = 0; (j < cols.size()); j++, itr++) {
                if (j != 0) {
                    //call function of getting child data
                    //getGrantorData(driver);
                    map.put(header[itr - 1], cols.get(j).getText());
                }
            }
            list.add(map);
            map = new TreeMap<>();
        }
        return list;
    }

    public JSONObject generateJson(ArrayList<SortedMap<String,String>> requiredData, String[] header) {
        JSONObject jsonObject = new JSONObject();

        JSONArray array = new JSONArray();

        JSONObject staticData = new JSONObject();
        staticData.put("type", "Lead_Search_c");
        staticData.put("referenceId", "ref");

        for(SortedMap<String, String> data : requiredData) {
            JSONObject dataElements = new JSONObject();
            dataElements.put("attributes", staticData);
            for (String str:header ) {
                dataElements.put(str,data.get(str));
            }
            array.put(dataElements);
        }
        jsonObject.put("records", array);
        return jsonObject;
    }

    public void getGrantorData(WebDriver driver)
    {
         /* //*[@id="DocList1_GridView_Document_ctl03_ButtonRow_Name/ Corporation_1"]
        //*[@id="DocList1_GridView_Document_ctl04_ButtonRow_Name/ Corporation_2"]
        //*[@id="DocList1_GridView_Document_ctl21_ButtonRow_Name/ Corporation_19"]

        2nd page: //*[@id="DocList1_GridView_Document_ctl02_ButtonRow_Name/ Corporation_0"]

        main logic: no of rows in a page
        */
        ArrayList<SortedMap<String,String>> list = new ArrayList<>();
        String[] key = {"Name","Type_c"};
        SortedMap<String,String> map = new TreeMap<>();
        driver.findElement(By.xpath("//*[@id=\"DocList1_GridView_Document_ctl02_ButtonRow_Name/ Corporation_0\"]")).click();
        WebElement table = driver.findElement(By.xpath("//*[@id=\"DocDetails1_GridView_GrantorGrantee\"]/tbody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        rows.remove(0);
        for (WebElement row:rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int i = 0; i < cols.size(); i++) {
                map.put(key[i],cols.get(i).getText());
            }
            list.add(map);
            map = new TreeMap<>();
        }
       grantorJson(list,key);
    }

    public void grantorJson(ArrayList<SortedMap<String,String>> list,String[] strings)
    {
        JSONObject mainObj = new JSONObject();
        JSONArray array = new JSONArray();
        for (SortedMap<String,String> data:list) {
            JSONObject object = new JSONObject();
            for (String str:strings) {
                object.put(str,data.get(str));
            }
            array.put(object);
        }
        mainObj.put("Grantor_c",array);

        System.out.println(mainObj);
    }

}
