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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;


public class dataProcessing {

public dataProcessing(){}

    public  String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath("//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr"));
        List<WebElement> headers = headerTag.findElements(By.tagName("th"));
        String[] header = new String[headers.size()];
        for (int i =0;i<headers.size();i++)
        {
            header[i]=headers.get(i).getText();
        }
        header = ArrayUtils.remove(header,0);
        return header;
    }

    public  void tableData(WebDriver driver,String fileName)
    {
        String[] headers = grabHeader(driver);
        List<HashMap<String,String>> tableDataContaint;
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

        List<JSONObject> jsonObj = new ArrayList<>();

        for(HashMap<String, String> data : tableDataContaint) {
            JSONObject obj = new JSONObject(data);
            jsonObj.add(obj);
        }

        try {
            File myObj = new File("C:\\JsonResponse\\"+fileName+".txt");
            if(myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter("C:\\JsonResponse\\"+fileName+".txt");
                myWriter.write(new JSONArray(jsonObj).toString());
                myWriter.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public List<HashMap<String,String>> appendToList(List<HashMap<String,String>> original,List<HashMap<String,String>> toBeAppend)
    {
        List<HashMap<String,String>> append = new ArrayList<>();
        Stream.of(original,toBeAppend).forEach(append::addAll);
        return append;
    }

    public List<HashMap<String,String>> grabData(WebDriver driver,String[] header)
    {
        List<HashMap<String,String>> list = new ArrayList<>();
        HashMap<String,String> map= new HashMap<>();
        WebElement table =  driver.findElement(By.xpath("//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int j = 0, itr = 0; (j < cols.size()); j++, itr++) {
                if (j != 0) {
                    map.put(header[itr - 1], cols.get(j).getText());
                }
            }
            list.add(map);
            map = new HashMap<>();
        }
        return list;
    }

}
