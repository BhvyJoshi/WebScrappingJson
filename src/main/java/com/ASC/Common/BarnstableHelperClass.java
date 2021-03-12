package com.ASC.Common;

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

public class BarnstableHelperClass {

    private boolean checkNext;
    public String[] grabHeader(WebDriver driver)
    {
        WebElement headerTag =  driver.findElement(By.xpath("//*[@id=\"search\"]/div/table/tbody/tr[1]"));
        List<WebElement> headers = headerTag.findElements(By.tagName("th"));
        String[] header = new String[headers.size()];
        for (int i =0;i<headers.size();i++)
        {
            header[i]=headers.get(i).getText();
        }
        return header;
    }

    public void fullTableData(WebDriver driver,String fileName)
    {
        String[] headers = grabHeader(driver);
        List<HashMap<String,String>> tableDataContaint;
        tableDataContaint = grabData(driver,headers);


       /* if(driver.findElement(By.xpath("//*[@id=\"search\"]/div/table/tbody/tr[12]/td")).getText() == "More names may be available")
            {  checkNext = true;}
*/
        while(checkNext){
            try{
                WebElement nextBtn = driver.findElement(By.xpath("//*[@id=\"search\"]/div/div[4]/div[1]/div/a[2]"));
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
    public List<HashMap<String,String>> grabData(WebDriver driver, String[] header)
    {
        List<HashMap<String,String>> list = new ArrayList<>();
        HashMap<String,String> map= new HashMap<>();
        WebElement table =  driver.findElement(By.xpath("//*[@id=\"search\"]/div/table/tbody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        rows.remove(0);
        if(rows.get(rows.size()-1).findElement(By.tagName("td")).getText()!="More names may be available")
        {
            checkNext = false;
        }
        rows.remove(rows.size()-1);

        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));

            if(cols.size()>1)
            {
                cols.remove(cols.size()-1);
                cols.remove(cols.size()-1);
            }

            for (int j = 0, itr = 0; (j < cols.size()); j++, itr++)
            {
                map.put(header[itr], cols.get(j).getText());
            }
            list.add(map);
            map = new HashMap<>();
        }
        return list;
    }
}
